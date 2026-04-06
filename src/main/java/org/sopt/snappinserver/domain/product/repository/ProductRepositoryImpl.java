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
import static org.sopt.snappinserver.domain.review.domain.entity.QReview.review;
import static org.sopt.snappinserver.domain.wish.domain.entity.QWishProduct.wishProduct;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQueryV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResultV2;
import org.sopt.snappinserver.domain.wish.domain.entity.QWishProduct;
import org.sopt.snappinserver.domain.product.service.dto.response.PopularMoodProductItemResult;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.sopt.snappinserver.global.enums.SortType;
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
                .leftJoin(review).on(review.product.id.eq(product.id))
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
    public List<GetProductCardResultV2> findProductCardsV2(
        GetProductListQueryV2 query,
        Map<MoodCategory, List<Long>> moodGroupMap,
        int size
    ) {
        QWishProduct wishProductSub = new QWishProduct("wishProductSub");
        SortType sort = query.sort() == null ? SortType.RECOMMENDED : query.sort();

        NumberExpression<Long> likeCount = wishProduct.id.countDistinct();
        JPQLQuery<Double> avgRatingSub = JPAExpressions
            .select(Expressions.numberTemplate(Double.class, "round(avg({0}), 1)", review.rating))
            .from(review)
            .where(review.product.id.eq(product.id));

        BooleanExpression liked = query.userId() == null
            ? Expressions.FALSE
            : JPAExpressions
                .selectOne()
                .from(wishProductSub)
                .where(
                    wishProductSub.product.id.eq(product.id),
                    wishProductSub.user.id.eq(query.userId())
                )
                .exists();

        List<ProductBaseRowV2> baseRows = jpaQueryFactory
            .select(Projections.constructor(
                ProductBaseRowV2.class,
                product.id,
                photo.imageUrl,
                liked,
                likeCount,
                avgRatingSub,
                product.title,
                review.id.countDistinct(),
                photographer.nickname,
                product.price
            ))
            .from(product)
            .join(product.photographer, photographer)
            .join(productPhoto).on(
                productPhoto.product.id.eq(product.id)
                    .and(productPhoto.displayOrder.eq(1))
            )
            .join(photo).on(photo.id.eq(productPhoto.photo.id))
            .leftJoin(wishProduct).on(wishProduct.product.id.eq(product.id))
            .leftJoin(review).on(review.product.id.eq(product.id))
            .where(
                photographerEq(query.photographerId()),
                snapCategoryEq(query.snapCategory()),
                placeCondition(query.placeId()),
                peopleCountCondition(query.peopleCount()),
                availableOnDate(query.date()),
                moodCategoryGroupedCondition(moodGroupMap),
                minPriceGoe(query.minPrice()),
                maxPriceLoe(query.maxPrice()),
                whereV2CursorCondition(sort, avgRatingSub, query.cursorAvgRating(), query.cursorId())
            )
            .groupBy(product.id, photo.imageUrl, product.title, photographer.nickname, product.price)
            .having(havingV2CursorCondition(sort, likeCount, query.cursorLikeCount(), query.cursorId()))
            .orderBy(buildV2OrderBy(sort, likeCount, avgRatingSub))
            .limit(size + 1)
            .fetch();

        if (baseRows.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = baseRows.stream().map(ProductBaseRowV2::id).toList();
        Map<Long, List<String>> moodNamesMap = fetchMoodNames(productIds);

        return baseRows.stream()
            .map(row -> new GetProductCardResultV2(
                row.id(), row.imageUrl(), row.isLiked(),
                row.likeCount() == null ? 0L : row.likeCount(),
                row.averageRating(), row.title(),
                row.reviewCount() == null ? 0L : row.reviewCount(),
                row.photographerName(), row.price(),
                moodNamesMap.getOrDefault(row.id(), List.of())
            ))
            .toList();
    }

    private BooleanExpression whereV2CursorCondition(
        SortType sort,
        JPQLQuery<Double> avgRatingSub,
        Double cursorAvgRating,
        Long cursorId
    ) {
        return switch (sort) {
            case LATEST -> cursorId != null ? product.id.lt(cursorId) : null;
            case POPULAR -> null;
            case RECOMMENDED -> {
                if (cursorAvgRating == null && cursorId == null) {
                    yield null;
                }
                BooleanExpression nullRating = Expressions.predicate(Ops.IS_NULL, avgRatingSub);
                if (cursorAvgRating == null) {
                    yield nullRating.and(product.id.lt(cursorId));
                }
                BooleanExpression ltAvg = Expressions.predicate(
                    Ops.LT, avgRatingSub, Expressions.constant(cursorAvgRating));
                BooleanExpression eqAvgAndLtId = Expressions.predicate(
                        Ops.EQ, avgRatingSub, Expressions.constant(cursorAvgRating))
                    .and(product.id.lt(cursorId));
                yield ltAvg.or(eqAvgAndLtId).or(nullRating);
            }
        };
    }

    private BooleanExpression havingV2CursorCondition(
        SortType sort,
        NumberExpression<Long> likeCount,
        Long cursorLikeCount,
        Long cursorId
    ) {
        if (sort != SortType.POPULAR || cursorLikeCount == null) {
            return null;
        }
        return likeCount.lt(cursorLikeCount)
            .or(likeCount.eq(cursorLikeCount).and(product.id.lt(cursorId)));
    }

    private OrderSpecifier<?>[] buildV2OrderBy(
        SortType sort,
        NumberExpression<Long> likeCount,
        JPQLQuery<Double> avgRatingSub
    ) {
        return switch (sort) {
            case LATEST -> new OrderSpecifier<?>[]{product.id.desc()};
            case POPULAR -> new OrderSpecifier<?>[]{likeCount.desc(), product.id.desc()};
            case RECOMMENDED -> new OrderSpecifier<?>[]{
                new OrderSpecifier<>(Order.DESC, avgRatingSub, OrderSpecifier.NullHandling.NullsLast),
                product.id.desc()
            };
        };
    }

    private BooleanExpression minPriceGoe(Integer minPrice) {
        return minPrice == null ? null : product.price.goe(minPrice);
    }

    private BooleanExpression maxPriceLoe(Integer maxPrice) {
        return maxPrice == null ? null : product.price.loe(maxPrice);
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

    @Override
    public List<Long> findTopProductIdsByMoodOrderByWishCount(Long moodId, int limit) {
        return jpaQueryFactory
            .select(product.id)
            .from(product)
            .join(productMood).on(productMood.product.id.eq(product.id))
            .where(productMood.mood.id.eq(moodId))
            .leftJoin(wishProduct).on(wishProduct.product.id.eq(product.id))
            .groupBy(product.id)
            .orderBy(wishProduct.id.count().desc(), product.id.desc())
            .limit(limit)
            .fetch();
    }

    @Override
    public List<PopularMoodProductItemResult> findPopularMoodProductItemsByIds(
        List<Long> productIds
    ) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        List<ProductBaseRow> rows = jpaQueryFactory
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
            .leftJoin(review).on(review.product.id.eq(product.id))
            .where(product.id.in(productIds))
            .groupBy(
                product.id,
                photo.imageUrl,
                product.title,
                photographer.nickname,
                product.price
            )
            .fetch();

        Map<Long, ProductBaseRow> byId = rows.stream()
            .collect(Collectors.toMap(ProductBaseRow::id, Function.identity()));

        return productIds.stream()
            .map(byId::get)
            .filter(Objects::nonNull)
            .map(row -> new PopularMoodProductItemResult(
                row.id(),
                row.imageUrl(),
                row.title(),
                row.rate(),
                row.reviewCount() == null ? 0L : row.reviewCount(),
                row.photographerName(),
                row.price(),
                false
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
