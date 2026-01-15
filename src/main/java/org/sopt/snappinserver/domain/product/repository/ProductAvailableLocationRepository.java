package org.sopt.snappinserver.domain.product.repository;

import java.util.List;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductAvailableLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAvailableLocationRepository
    extends JpaRepository<ProductAvailableLocation, Long> {

    List<ProductAvailableLocation> findByProduct(Product product);
}
