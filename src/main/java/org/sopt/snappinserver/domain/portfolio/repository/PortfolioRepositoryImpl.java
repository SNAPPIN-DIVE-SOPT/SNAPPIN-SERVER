package org.sopt.snappinserver.domain.portfolio.repository;

import static org.sopt.snappinserver.domain.photo.domain.entity.QPhoto.photo;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolio.portfolio;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolioPhoto.portfolioPhoto;
import static org.sopt.snappinserver.domain.portfolio.domain.entity.QPortfolioPlace.portfolioPlace;
import static org.sopt.snappinserver.domain.wish.domain.entity.QWishPortfolio.wishPortfolio;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PortfolioRepositoryImpl implements PortfolioRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<String> findBestPortfolioImageByPlaceId(Long placeId) {
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
                portfolio.id.desc()
            )
            .limit(1)
            .fetchOne();

        return Optional.ofNullable(imageKey);
    }
}
