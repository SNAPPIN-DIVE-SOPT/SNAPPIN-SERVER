package org.sopt.snappinserver.domain.product.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProductIdAndProductOptionCategoryIn(
        Long productId,
        List<ProductOptionCategory> categories
    );

    Optional<ProductOption> findByProductAndProductOptionCategory(
        Product product,
        ProductOptionCategory productOptionCategory
    );
}
