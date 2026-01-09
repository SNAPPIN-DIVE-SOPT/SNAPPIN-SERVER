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
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PortfolioPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio_place_seq_gen")
    @SequenceGenerator(
        name = "portfolio_place_seq_gen",
        sequenceName = "portfolio_place_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder(access = AccessLevel.PRIVATE)
    private PortfolioPlace(Portfolio portfolio, Place place) {
        this.portfolio = portfolio;
        this.place = place;
    }

    public static PortfolioPlace create(Portfolio portfolio, Place place) {
        return PortfolioPlace.builder()
            .portfolio(portfolio)
            .place(place)
            .build();
    }

}
