package org.sopt.snappinserver.domain.wish.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.wish.domain.entity.WishProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishProductRepository extends JpaRepository<WishProduct, Long> {
    Optional<WishProduct> findByUserAndProduct(User user, Product product);
}
