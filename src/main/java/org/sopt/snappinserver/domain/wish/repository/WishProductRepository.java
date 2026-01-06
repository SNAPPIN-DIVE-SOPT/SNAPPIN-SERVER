package org.sopt.snappinserver.domain.wish.repository;

import org.sopt.snappinserver.domain.wish.domain.entity.WishProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishProductRepository extends JpaRepository<WishProduct, Long> {

}
