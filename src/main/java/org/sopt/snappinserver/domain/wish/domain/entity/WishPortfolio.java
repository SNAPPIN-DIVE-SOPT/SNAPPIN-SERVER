package org.sopt.snappinserver.domain.wish.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_wish_user_portfolio",
        columnNames = {"user_id", "portfolio_id"}
    )
})
public class WishPortfolio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Builder(access = AccessLevel.PRIVATE)
    private WishPortfolio(User user, Portfolio portfolio) {
        this.user = user;
        this.portfolio = portfolio;
    }

    public static WishPortfolio create(User user, Portfolio portfolio) {
        validateWishPortfolio(user, portfolio);
        return WishPortfolio.builder()
            .user(user)
            .portfolio(portfolio)
            .build();
    }

    private static void validateWishPortfolio(User user, Portfolio portfolio) {
        validateUserExists(user);
        validatePortfolioExists(portfolio);
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new WishException(WishErrorCode.USER_REQUIRED);
        }
    }

    private static void validatePortfolioExists(Portfolio portfolio) {
        if (portfolio == null) {
            throw new WishException(WishErrorCode.PORTFOLIO_REQUIRED);
        }
    }
}
