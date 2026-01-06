package org.sopt.snappinserver.domain.product.domain.entity;

import jakarta.persistence.Column;
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
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @Column(nullable = false)
    private int displayOrder;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductPhoto(Product product, Photo photo, int displayOrder) {
        this.product = product;
        this.photo = photo;
        this.displayOrder = displayOrder;
    }

    public ProductPhoto create(Product product, Photo photo, int displayOrder) {
        return ProductPhoto.builder()
            .product(product)
            .photo(photo)
            .displayOrder(displayOrder)
            .build();
    }

}
