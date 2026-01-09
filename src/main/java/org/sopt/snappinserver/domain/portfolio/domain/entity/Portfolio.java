package org.sopt.snappinserver.domain.portfolio.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalTime;
import javax.sound.sampled.Port;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.domain.exception.PortfolioErrorCode;
import org.sopt.snappinserver.domain.portfolio.domain.exception.PortfolioException;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Portfolio {

    private static final int MAX_DESCRIPTION_LENGTH = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio_seq_gen")
    @SequenceGenerator(
        name = "portfolio_seq_gen",
        sequenceName = "portfolio_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = MAX_DESCRIPTION_LENGTH)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnapCategory snapCategory;

    @Column(nullable = false)
    private LocalTime startsAt;

    @Column(nullable = false)
    private LocalTime endsAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Portfolio(
        Product product,
        String description,
        SnapCategory snapCategory,
        LocalTime startsAt,
        LocalTime endsAt
    ) {
        this.product = product;
        this.description = description;
        this.snapCategory = snapCategory;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public static Portfolio create(
        Product product,
        String description,
        SnapCategory snapCategory,
        LocalTime startAt,
        LocalTime endsAt
    ) {
        validatePortfolio(product, snapCategory, startAt, endsAt);
        return Portfolio.builder()
            .product(product)
            .description(description)
            .snapCategory(snapCategory)
            .startsAt(startAt)
            .endsAt(endsAt)
            .build();
    }

    private static void validatePortfolio(
        Product product,
        SnapCategory snapCategory,
        LocalTime startAt,
        LocalTime endsAt
    ) {
        validateProductExists(product);
        validateSnapCategoryExists(snapCategory);
        validateTimeRange(startAt, endsAt);
    }

    private static void validateProductExists(Product product) {
        if (product == null) {
            throw new PortfolioException(PortfolioErrorCode.PRODUCT_REQUIRED);
        }
    }

    private static void validateSnapCategoryExists(SnapCategory snapCategory) {
        if (snapCategory == null) {
            throw new PortfolioException(PortfolioErrorCode.SNAP_CATEGORY_REQUIRED);
        }
    }

    private static void validateTimeRange(LocalTime startsAt, LocalTime endsAt) {
        validateStartsAt(startsAt);
        validateEndsAtExists(endsAt);
        validateTimeOrder(startsAt, endsAt);
    }

    private static void validateStartsAt(LocalTime startsAt) {
        if(startsAt == null) {
            throw new PortfolioException(PortfolioErrorCode.STARTS_AT_REQUIRED);
        }
    }

    private static void validateEndsAtExists(LocalTime endsAt) {
        if(endsAt == null) {
            throw new PortfolioException(PortfolioErrorCode.ENDS_AT_REQUIRED);
        }
    }

    private static void validateTimeOrder(LocalTime startsAt, LocalTime endsAt) {
        if (startsAt.isAfter(endsAt) || startsAt.equals(endsAt)) {
            throw new PortfolioException(PortfolioErrorCode.STARTS_AT_AFTER_ENDS_AT);
        }
    }
}
