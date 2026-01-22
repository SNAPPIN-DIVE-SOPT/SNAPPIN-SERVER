package org.sopt.snappinserver.domain.product.repository;

import static org.sopt.snappinserver.domain.mood.domain.entity.QMood.mood;
import static org.sopt.snappinserver.domain.photo.domain.entity.QPhoto.photo;
import static org.sopt.snappinserver.domain.photographer.domain.entity.QPhotographer.photographer;
import static org.sopt.snappinserver.domain.photographer.domain.entity.QPhotographerSchedule.photographerSchedule;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolio.portfolio;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolioPlace.portfolioPlace;
import static org.sopt.snappinserver.domain.product.domain.entity.QProduct.product;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
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
import org.sopt.snappinserver.global.enums.WeekDay;
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
                        Expressions.numberTemplate(
                            Double.class,
                            "round({0}, 1)",
                            review.rating.avg().coalesce(0.0)
                        ),
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
                    availableOnDate(query.date()),
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

    @Override
    public List<String> findProductMoods(Long productId) {
        return jpaQueryFactory
            .select(mood.name)
            .from(productMood)
            .join(productMood.mood, mood)
            .where(productMood.product.id.eq(productId))
            .orderBy(productMood.id.asc())
            .fetch();
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
                .select(portfolio.product.id)
                .from(portfolio)
                .join(portfolioPlace)
                .on(portfolioPlace.portfolio.id.eq(portfolio.id))
                .where(portfolioPlace.place.id.eq(placeId))
        );
    }

    private BooleanExpression availableOnDate(LocalDate date) {
        if (date == null) {
            return null;
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return JPAExpressions
            .selectOne()
            .from(photographerSchedule)
            .where(
                photographerSchedule.photographer.id.eq(product.photographer.id),
                photographerSchedule.weekDay.eq(WeekDay.from(dayOfWeek)),
                photographerSchedule.dayOff.isTrue()
            )
            .notExists();
    }



    private BooleanExpression peopleCountCondition(Integer peopleCount) {
        if (peopleCount == null) {
            return null;
        }

        BooleanExpression minOk =
            JPAExpressions
                .selectOne()
                .from(productOption)
                .where(
                    productOption.product.id.eq(product.id),
                    productOption.productOptionCategory.eq(ProductOptionCategory.MIN_PEOPLE),
                    productOption.answer.castToNum(Integer.class).loe(peopleCount)
                )
                .exists();

        BooleanExpression maxOk =
            JPAExpressions
                .selectOne()
                .from(productOption)
                .where(
                    productOption.product.id.eq(product.id),
                    productOption.productOptionCategory.eq(ProductOptionCategory.MAX_PEOPLE),
                    productOption.answer.castToNum(Integer.class).goe(peopleCount)
                )
                .exists();

        BooleanExpression noLimit =
            JPAExpressions
                .selectOne()
                .from(productOption)
                .where(
                    productOption.product.id.eq(product.id),
                    productOption.productOptionCategory.in(
                        ProductOptionCategory.MIN_PEOPLE,
                        ProductOptionCategory.MAX_PEOPLE
                    )
                )
                .notExists();

        return minOk.or(maxOk).or(noLimit);
    }

    private Predicate moodCategoryGroupedCondition(
        Map<MoodCategory, List<Long>> moodGroupMap
    ) {
        if (moodGroupMap == null || moodGroupMap.isEmpty()) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();

        for (Entry<MoodCategory, List<Long>> entry : moodGroupMap.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }

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
            .orderBy(productMood.id.asc())
            .fetch()) {
            String s = tuple.get(mood.name);
            map.computeIfAbsent(tuple.get(productMood.product.id), k -> new ArrayList<>()).add(s);
        }
        return map;
    }
}
