package org.sopt.snappinserver.domain.product.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.AvailableLocation;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductAvailableLocation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "available_location_id", nullable = false)
    private AvailableLocation availableLocation;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductAvailableLocation(Product product, AvailableLocation availableLocation) {
        this.product = product;
        this.availableLocation = availableLocation;
    }

    public static ProductAvailableLocation create(
        Product product,
        AvailableLocation availableLocation
    ) {
        return ProductAvailableLocation.builder()
            .product(product)
            .availableLocation(availableLocation)
            .build();
    }
}
