package org.sopt.snappinserver.domain.product.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {

    Optional<ProductPhoto> findFirstByProductOrderByDisplayOrderAsc(Product product);

    @Query("""
        select pp
        from ProductPhoto pp
        join fetch pp.photo ph
        where pp.product.id in :productIds
          and pp.displayOrder = 0
    """)
    List<ProductPhoto> findThumbnails(@Param("productIds") List<Long> productIds);

    default Map<Long, String> findThumbnailByProductIds(List<Long> productIds) {
        return findThumbnails(productIds).stream()
            .collect(Collectors.toMap(
                pp -> pp.getProduct().getId(),
                pp -> pp.getPhoto().getImageUrl()
            ));
    }

}
