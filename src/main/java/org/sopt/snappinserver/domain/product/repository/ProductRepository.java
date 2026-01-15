package org.sopt.snappinserver.domain.product.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p.price from Product p where p.id = :productId")
    Optional<Integer> findProductPriceById(@Param("productId") Long productId);
}
