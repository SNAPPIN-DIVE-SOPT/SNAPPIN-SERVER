package org.sopt.snappinserver.domain.product.repository;

import static org.sopt.snappinserver.domain.mood.domain.entity.QMood.mood;
import static org.sopt.snappinserver.domain.photo.domain.entity.QPhoto.photo;
import static org.sopt.snappinserver.domain.photographer.domain.entity.QPhotographer.photographer;
import static org.sopt.snappinserver.domain.product.domain.entity.QProduct.product;
import static org.sopt.snappinserver.domain.product.domain.entity.QProductAvailableLocation.productAvailableLocation;
import static org.sopt.snappinserver.domain.product.domain.entity.QProductMood.productMood;
import static org.sopt.snappinserver.domain.product.domain.entity.QProductOption.productOption;
import static org.sopt.snappinserver.domain.product.domain.entity.QProductPhoto.productPhoto;
import static org.sopt.snappinserver.domain.reservation.domain.entity.QReservation.reservation;
import static org.sopt.snappinserver.domain.review.domain.entity.QReview.review;
import static org.sopt.snappinserver.domain.wish.domain.entity.QWishProduct.wishProduct;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResult;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final int PRODUCT_LIST_QUERY_SIZE = 10;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public LikeStatusProjection findLikeStatus(Long productId, Long userId) {
        NumberExpression<Integer> likeCount = wishProduct.count().intValue();

        if (userId == null) {
            LikeStatusProjection result = jpaQueryFactory
                .select(
                    Projections.constructor(
                        LikeStatusProjection.class,
                        likeCount,
                        Expressions.FALSE
                    )
                )
                .from(wishProduct)
                .where(wishProduct.product.id.eq(productId))
                .fetchOne();
            return result != null ? result : new LikeStatusProjection(0, false);
        }

        BooleanExpression liked =
            JPAExpressions
                .selectOne()
                .from(wishProduct)
                .where(
                    wishProduct.product.id.eq(productId),
                    wishProduct.user.id.eq(userId)
                )
                .exists();

        LikeStatusProjection result = jpaQueryFactory
            .select(
                Projections.constructor(
                    LikeStatusProjection.class,
                    likeCount,
                    liked
                )
            )
            .from(wishProduct)
            .where(wishProduct.product.id.eq(productId))
            .fetchOne();
        return result != null ? result : new LikeStatusProjection(0, false);
    }

    @Override
    public List<GetProductCardResult> findProducts(
        GetProductListQuery query,
        Map<MoodCategory, List<Long>> moodGroupMap
    ) {
        List<ProductBaseRow> baseRows =
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        ProductBaseRow.class,
                        product.id,
                        photo.imageUrl,
                        product.title,
                        review.rating.avg().coalesce(0.0),
                        review.id.countDistinct(),
                        photographer.nickname,
                        product.price
                    )
                )
                .from(product)
                .join(productPhoto).on(
                    productPhoto.product.id.eq(product.id).and(productPhoto.displayOrder.eq(1))
                )
                .join(photo).on(photo.id.eq(productPhoto.photo.id))
                .leftJoin(reservation).on(reservation.product.id.eq(product.id))
                .leftJoin(review).on(review.reservation.id.eq(reservation.id))
                .where(
                    cursorLt(query.cursor()),
                    photographerEq(query.photographerId()),
                    snapCategoryEq(query.snapCategory()),
                    placeCondition(query.placeId()),
                    peopleCountCondition(query.peopleCount()),
                    moodCategoryGroupedCondition(moodGroupMap)
                )
                .groupBy(
                    product.id,
                    photo.imageUrl,
                    product.title,
                    photographer.nickname,
                    product.price
                )
                .orderBy(product.id.desc())
                .limit(PRODUCT_LIST_QUERY_SIZE + 1)
                .fetch();

        if (baseRows.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = baseRows.stream()
            .map(ProductBaseRow::id)
            .toList();

        Map<Long, List<String>> moodNamesMap = fetchMoodNames(productIds);

        return baseRows.stream()
            .map(row -> new GetProductCardResult(
                row.id(),
                row.imageUrl(),
                row.title(),
                row.rate(),
                row.reviewCount(),
                row.photographerName(),
                row.price(),
                moodNamesMap.getOrDefault(row.id(), List.of())
            ))
            .toList();
    }

    private BooleanExpression cursorLt(Long cursor) {
        return cursor == null ? null : product.id.lt(cursor);
    }

    private BooleanExpression photographerEq(Long photographerId) {
        return photographerId == null ? null : photographer.id.eq(photographerId);
    }

    private BooleanExpression snapCategoryEq(SnapCategory snapCategory) {
        return snapCategory == null ? null : product.snapCategory.eq(snapCategory);
    }

    private BooleanExpression placeCondition(Long placeId) {
        if (placeId == null) {
            return null;
        }

        return product.id.in(
            JPAExpressions
                .select(productAvailableLocation.product.id)
                .from(productAvailableLocation)
                .where(productAvailableLocation.availableLocation.id.eq(placeId))
        );
    }

    private BooleanExpression peopleCountCondition(Integer peopleCount) {
        if (peopleCount == null) {
            return null;
        }
        return product.id.in(
            JPAExpressions
                .select(productOption.product.id)
                .from(productOption)
                .where(
                    productOption.productOptionCategory.in(ProductOptionCategory.MIN_PEOPLE,
                        ProductOptionCategory.MAX_PEOPLE)
                )
        );
    }

    private Predicate moodCategoryGroupedCondition(
        Map<MoodCategory, List<Long>> moodGroupMap
    ) {
        if (moodGroupMap == null || moodGroupMap.isEmpty()) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();

        for (Entry<MoodCategory, List<Long>> entry : moodGroupMap.entrySet()) {
            builder.and(
                product.id.in(
                    JPAExpressions
                        .select(productMood.product.id)
                        .from(productMood)
                        .join(productMood.mood, mood)
                        .where(
                            mood.category.eq(entry.getKey()),
                            mood.id.in(entry.getValue())
                        )
                )
            );
        }

        return builder;
    }

    private Map<Long, List<String>> fetchMoodNames(List<Long> productIds) {
        Map<Long, List<String>> map = new HashMap<>();
        for (Tuple tuple : jpaQueryFactory
            .select(productMood.product.id, mood.name)
            .from(productMood)
            .join(mood).on(productMood.mood.id.eq(mood.id))
            .where(productMood.product.id.in(productIds))
            .fetch()) {
            String s = tuple.get(mood.name);
            map.computeIfAbsent(tuple.get(productMood.product.id), k -> new ArrayList<>()).add(s);
        }
        return map;
    }
}
