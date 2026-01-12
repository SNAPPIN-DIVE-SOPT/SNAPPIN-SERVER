package org.sopt.snappinserver.domain.product.repository;

import java.util.List;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductMood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMoodRepository extends JpaRepository<ProductMood, Long> {

    List<ProductMood> findAllByProduct(Product product);
}
