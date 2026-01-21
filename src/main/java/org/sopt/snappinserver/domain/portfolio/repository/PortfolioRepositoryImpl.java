package org.sopt.snappinserver.domain.portfolio.repository;

import static org.sopt.snappinserver.domain.mood.domain.entity.QMood.mood;
import static org.sopt.snappinserver.domain.photo.domain.entity.QPhoto.photo;
import static org.sopt.snappinserver.domain.photographer.domain.entity.QPhotographer.photographer;
import static org.sopt.snappinserver.domain.photographer.domain.entity.QPhotographerAvailableLocation.photographerAvailableLocation;
import static org.sopt.snappinserver.domain.photographer.domain.entity.QPhotographerSpecialty.photographerSpecialty;
import static org.sopt.snappinserver.domain.place.domain.entity.QAvailableLocation.availableLocation;
import static org.sopt.snappinserver.domain.place.domain.entity.QPlace.place;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolio.portfolio;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolioMood.portfolioMood;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolioPhoto.portfolioPhoto;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolioPlace.portfolioPlace;
import static org.sopt.snappinserver.domain.product.domain.entity.QProduct.product;
import static org.sopt.snappinserver.domain.product.domain.entity.QProductMood.productMood;
import static org.sopt.snappinserver.domain.product.domain.entity.QProductPhoto.productPhoto;
import static org.sopt.snappinserver.domain.wish.domain.entity.QWishPortfolio.wishPortfolio;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQuery;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.PortfolioDetailProjection;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PortfolioRepositoryImpl implements PortfolioRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<String> findBestPortfolioImageByPlaceId(Long placeId) {
        NumberExpression<Double> random = Expressions.numberTemplate(
            Double.class,
            "function('random')"
        );

        String imageKey = jpaQueryFactory
            .select(photo.imageUrl)
            .from(portfolio)
            .join(portfolioPlace).on(portfolioPlace.portfolio.eq(portfolio))
            .leftJoin(wishPortfolio).on(wishPortfolio.portfolio.eq(portfolio))
            .join(portfolioPhoto).on(portfolioPhoto.portfolio.eq(portfolio))
            .join(portfolioPhoto.photo, photo)
            .where(
                portfolioPlace.place.id.eq(placeId),
                portfolioPhoto.displayOrder.eq(1)
            )
            .groupBy(portfolio.id, photo.imageUrl)
            .orderBy(
                wishPortfolio.count().desc(),
                random.asc()
            )
            .limit(1)
            .fetchOne();

        return Optional.ofNullable(imageKey);
    }

    @Override
    public PortfolioDetailProjection findDetail(Long portfolioId) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    PortfolioDetailProjection.class,
                    portfolio.id,
                    portfolio.description,
                    portfolio.snapCategory,
                    portfolio.startsAt.stringValue(),
                    place.name,
                    photographer.id,
                    photographer.name,
                    product.id,
                    product.title,
                    product.price
                )
            )
            .from(portfolio)
            .join(portfolio.product, product)
            .join(product.photographer, photographer)
            .leftJoin(portfolioPlace).on(portfolioPlace.portfolio.eq(portfolio))
            .leftJoin(portfolioPlace.place, place)
            .where(portfolio.id.eq(portfolioId))
            .fetchOne();
    }

    @Override
    public LikeStatusProjection findLikeStatus(Long portfolioId, Long userId) {
        NumberExpression<Integer> likeCount = wishPortfolio.count().intValue();

        if (userId == null) {
            return jpaQueryFactory
                .select(
                    Projections.constructor(
                        LikeStatusProjection.class,
                        likeCount,
                        Expressions.FALSE
                    )
                )
                .from(wishPortfolio)
                .where(wishPortfolio.portfolio.id.eq(portfolioId))
                .fetchOne();
        }

        BooleanExpression liked =
            JPAExpressions
                .selectOne()
                .from(wishPortfolio)
                .where(
                    wishPortfolio.portfolio.id.eq(portfolioId),
                    wishPortfolio.user.id.eq(userId)
                )
                .exists();

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    LikeStatusProjection.class,
                    likeCount,
                    liked
                )
            )
            .from(wishPortfolio)
            .where(wishPortfolio.portfolio.id.eq(portfolioId))
            .fetchOne();
    }

    @Override
    public List<String> findPortfolioImageUrls(Long portfolioId) {
        return jpaQueryFactory
            .select(portfolioPhoto.photo.imageUrl)
            .from(portfolioPhoto)
            .join(portfolioPhoto.photo)
            .where(portfolioPhoto.portfolio.id.eq(portfolioId))
            .orderBy(portfolioPhoto.displayOrder.asc())
            .fetch();
    }

    @Override
    public List<String> findPortfolioMoods(Long portfolioId) {
        return jpaQueryFactory
            .select(mood.name)
            .from(portfolioMood)
            .join(portfolioMood.mood, mood)
            .where(portfolioMood.portfolio.id.eq(portfolioId))
            .fetch();
    }

    @Override
    public List<String> findProductMoods(Long productId) {
        return jpaQueryFactory
            .select(mood.name)
            .from(productMood)
            .join(productMood.mood, mood)
            .where(productMood.product.id.eq(productId))
            .fetch();
    }

    @Override
    public String findProductThumbnailUrl(Long productId) {
        return jpaQueryFactory
            .select(productPhoto.photo.imageUrl)
            .from(productPhoto)
            .join(productPhoto.photo)
            .where(
                productPhoto.product.id.eq(productId),
                productPhoto.displayOrder.eq(1)
            )
            .fetchOne();
    }

    @Override
    public List<SnapCategory> findPhotographerSpecialties(Long photographerId) {
        return jpaQueryFactory
            .select(photographerSpecialty.specialty)
            .from(photographerSpecialty)
            .where(photographerSpecialty.photographer.id.eq(photographerId))
            .fetch();
    }

    @Override
    public List<String> findPhotographerAvailableLocations(Long photographerId) {
        return jpaQueryFactory
            .select(
                Expressions.stringTemplate(
                    "case " +
                        "when {1} is null or {1} = '' " +
                        "then {0} " +
                        "else concat({0}, ' ', {1}) end",
                    availableLocation.sido,
                    availableLocation.sigungu
                )
            )
            .from(photographerAvailableLocation)
            .join(
                photographerAvailableLocation.availableLocation,
                availableLocation
            )
            .where(
                photographerAvailableLocation.photographer.id.eq(photographerId)
            )
            .fetch();
    }

    @Override
    public List<Portfolio> findByMatchCountExcludeIds(
        List<Long> curatedMoodIds,
        Set<Long> excludedPortfolioIds,
        int matchCount,
        int limit
    ) {
        return jpaQueryFactory
            .select(portfolio)
            .from(portfolioMood)
            .join(portfolioMood.portfolio, portfolio)
            .where(
                portfolioMood.mood.id.in(curatedMoodIds),
                excludedPortfolioIds.isEmpty()
                    ? null
                    : portfolio.id.notIn(excludedPortfolioIds)
            )
            .groupBy(portfolio.id)
            .having(portfolioMood.mood.id.countDistinct().eq((long) matchCount))
            .orderBy(Expressions.numberTemplate(Double.class, "function('random')").asc())
            .limit(limit)
            .fetch();
    }

    @Override
    public List<GetPortfolioCardResult> findPortfolioCards(
        Long cursor,
        GetPortfolioListQuery query,
        Map<MoodCategory, List<Long>> moodGroupMap,
        int size
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GetPortfolioCardResult.class,
                    portfolio.id,
                    portfolioPhoto.photo.imageUrl
                )
            )
            .from(portfolio)
            .join(portfolio.product, product)
            .join(product.photographer, photographer)
            .join(portfolioPhoto).on(
                portfolioPhoto.portfolio.id.eq(portfolio.id)
                    .and(portfolioPhoto.displayOrder.eq(1))
            )
            .join(portfolioPhoto.photo, photo)
            .leftJoin(portfolioPlace).on(portfolioPlace.portfolio.id.eq(portfolio.id))
            .leftJoin(portfolioMood).on(portfolioMood.portfolio.id.eq(portfolio.id))
            .where(
                cursorLt(cursor),
                moodCategoryGroupedCondition(moodGroupMap),
                productIdEq(query.productId()),
                photographerIdEq(query.photographerId()),
                snapCategoryEq(query.snapCategory()),
                placeEq(query.placeId())
            )
            .distinct()
            .orderBy(portfolio.id.desc())
            .limit(size + 1)
            .fetch();
    }

    private BooleanExpression cursorLt(Long cursor) {
        return cursor == null ? null : portfolio.id.lt(cursor);
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
                portfolio.id.in(
                    JPAExpressions
                        .select(portfolioMood.portfolio.id)
                        .from(portfolioMood)
                        .join(portfolioMood.mood, mood)
                        .where(
                            mood.category.eq(entry.getKey()),
                            mood.id.in(entry.getValue())
                        )
                )
            );
        }

        return builder;
    }

    private BooleanExpression productIdEq(Long productId) {
        return productId == null ? null : product.id.eq(productId);
    }

    private BooleanExpression photographerIdEq(Long photographerId) {
        return photographerId == null ? null : photographer.id.eq(photographerId);
    }

    private BooleanExpression snapCategoryEq(SnapCategory category) {
        return category == null ? null : portfolio.snapCategory.eq(category);
    }

    private BooleanExpression placeEq(Long placeId) {
        return placeId == null ? null : portfolioPlace.place.id.eq(placeId);
    }

}
