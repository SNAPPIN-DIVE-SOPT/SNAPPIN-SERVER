package org.sopt.snappinserver.domain.product.repository;

import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
