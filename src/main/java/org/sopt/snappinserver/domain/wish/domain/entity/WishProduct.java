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
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_wish_user_product",
        columnNames = {"user_id", "product_id"}
    )
})
public class WishProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder(access = AccessLevel.PRIVATE)
    private WishProduct(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public static WishProduct create(User user, Product product) {
        validateWishProduct(user, product);
        return WishProduct.builder()
            .user(user)
            .product(product)
            .build();
    }

    private static void validateWishProduct(User user, Product product) {
        validateUserExists(user);
        validateProductExists(product);
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new WishException(WishErrorCode.USER_REQUIRED);
        }
    }

    private static void validateProductExists(Product product) {
        if (product == null) {
            throw new WishException(WishErrorCode.PRODUCT_REQUIRED);
        }
    }

}
