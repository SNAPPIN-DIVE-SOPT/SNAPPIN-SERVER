package org.sopt.snappinserver.domain.portfolio.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PortfolioPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio_photo_seq_gen")
    @SequenceGenerator(
        name = "portfolio_photo_seq_gen",
        sequenceName = "portfolio_photo_seq_",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    private int displayOrder;

    @Builder(access = AccessLevel.PRIVATE)
    private PortfolioPhoto(Portfolio portfolio, Photo photo, int displayOrder) {
        this.portfolio = portfolio;
        this.photo = photo;
        this.displayOrder = displayOrder;
    }

    public static PortfolioPhoto create(Portfolio portfolio, Photo photo, int displayOrder) {
        return PortfolioPhoto.builder()
            .portfolio(portfolio)
            .photo(photo)
            .displayOrder(displayOrder)
            .build();
    }

}
