package org.sopt.snappinserver.domain.product.repository;

import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

}
