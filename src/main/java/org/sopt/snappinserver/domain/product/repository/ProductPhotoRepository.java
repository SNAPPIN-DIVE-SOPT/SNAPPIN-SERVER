package org.sopt.snappinserver.domain.product.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductPhoto;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPhotoRepository {

    Optional<ProductPhoto> findFirstByProductOrderByDisplayOrderAsc(Product product);

}
