package org.sopt.snappinserver.domain.portfolio.repository;

import static org.sopt.snappinserver.domain.mood.domain.entity.QMood.mood;
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

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.PortfolioDetailProjection;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PortfolioRepositoryImpl implements PortfolioRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PortfolioDetailProjection findDetail(Long portfolioId) {
        return queryFactory
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
            .join(portfolioPlace).on(portfolioPlace.portfolio.eq(portfolio))
            .join(portfolioPlace.place, place)
            .where(portfolio.id.eq(portfolioId))
            .fetchOne();
    }


    @Override
    public LikeStatusProjection findLikeStatus(Long portfolioId, Long userId) {
        NumberExpression<Integer> likeCount = wishPortfolio.count().intValue();

        if (userId == null) {
            return queryFactory
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

        return queryFactory
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
        return queryFactory
            .select(portfolioPhoto.photo.imageUrl)
            .from(portfolioPhoto)
            .join(portfolioPhoto.photo)
            .where(portfolioPhoto.portfolio.id.eq(portfolioId))
            .orderBy(portfolioPhoto.displayOrder.asc())
            .fetch();
    }

    @Override
    public List<String> findPortfolioMoods(Long portfolioId) {
        return queryFactory
            .select(mood.name)
            .from(portfolioMood)
            .join(portfolioMood.mood, mood)
            .where(portfolioMood.portfolio.id.eq(portfolioId))
            .fetch();
    }

    @Override
    public List<String> findProductMoods(Long productId) {
        return queryFactory
            .select(mood.name)
            .from(productMood)
            .join(productMood.mood, mood)
            .where(productMood.product.id.eq(productId))
            .fetch();
    }

    @Override
    public String findProductThumbnailUrl(Long productId) {
        return queryFactory
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
    public List<String> findPhotographerSpecialties(Long photographerId) {
        return queryFactory
            .select(photographerSpecialty.specialty.stringValue())
            .from(photographerSpecialty)
            .where(photographerSpecialty.photographer.id.eq(photographerId))
            .fetch();
    }

    @Override
    public List<String> findPhotographerAvailableLocations(Long photographerId) {
        return queryFactory
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

}
