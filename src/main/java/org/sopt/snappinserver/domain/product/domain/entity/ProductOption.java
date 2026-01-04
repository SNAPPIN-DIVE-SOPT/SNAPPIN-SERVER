package org.sopt.snappinserver.domain.product.domain.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_product_option_product_category",
        columnNames = {"product_id", "product_option_category"}
    )
})
public class ProductOption extends BaseEntity {

    private static final int MAX_ANSWER_LENGTH = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_option_category", nullable = false)
    private ProductOptionCategory productOptionCategory;

    @Column(nullable = false, length = MAX_ANSWER_LENGTH)
    private String answer;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductOption(
        Product product,
        ProductOptionCategory productOptionCategory,
        String answer
    ) {
        this.product = product;
        this.productOptionCategory = productOptionCategory;
        this.answer = answer;
    }

    public static ProductOption create(
        Product product,
        ProductOptionCategory productOptionCategory,
        String answer
    ) {
        validateProductOption(product, productOptionCategory, answer);
        return ProductOption.builder()
            .product(product)
            .productOptionCategory(productOptionCategory)
            .answer(answer)
            .build();
    }

    private static void validateProductOption(
        Product product,
        ProductOptionCategory productOptionCategory,
        String answer
    ) {
        validateProductExists(product);
        validateProductOptionCategoryExists(productOptionCategory);
        validateAnswer(answer);
    }

    private static void validateProductExists(Product product) {
        if (product == null) {
            throw new ProductException(ProductErrorCode.PRODUCT_REQUIRED);
        }
    }

    private static void validateProductOptionCategoryExists(
        ProductOptionCategory productOptionCategory
    ) {
        if (productOptionCategory == null) {
            throw new ProductException(ProductErrorCode.PRODUCT_OPTION_CATEGORY_REQUIRED);
        }
    }

    private static void validateAnswer(String answer) {
        validateAnswerExists(answer);
        validateAnswerLength(answer);
    }

    private static void validateAnswerExists(String answer) {
        if (answer == null || answer.isBlank()) {
            throw new ProductException(ProductErrorCode.ANSWER_REQUIRED);
        }
    }

    private static void validateAnswerLength(String answer) {
        if (answer.length() > MAX_ANSWER_LENGTH) {
            throw new ProductException(ProductErrorCode.ANSWER_TOO_LONG);
        }
    }
}
