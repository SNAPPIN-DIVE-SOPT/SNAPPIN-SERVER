package org.sopt.snappinserver.domain.product.domain.entity;

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
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductMood extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_mood_seq_gen")
    @SequenceGenerator(
        name = "product_mood_seq_gen",
        sequenceName = "product_mood_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_id", nullable = false)
    private Mood mood;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductMood(Product product, Mood mood) {
        this.product = product;
        this.mood = mood;
    }

    public static ProductMood create(Product product, Mood mood) {
        return ProductMood.builder()
            .product(product)
            .mood(mood)
            .build();
    }

}
