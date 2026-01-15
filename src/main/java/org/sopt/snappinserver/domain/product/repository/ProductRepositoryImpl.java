package org.sopt.snappinserver.domain.product.repository;

import static org.sopt.snappinserver.domain.wish.domain.entity.QWishProduct.wishProduct;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public LikeStatusProjection findLikeStatus(Long productId, Long userId) {
        NumberExpression<Integer> likeCount = wishProduct.count().intValue();

        if (userId == null) {
            LikeStatusProjection result = jpaQueryFactory
                .select(
                    Projections.constructor(
                        LikeStatusProjection.class,
                        likeCount,
                        Expressions.FALSE
                    )
                )
                .from(wishProduct)
                .where(wishProduct.product.id.eq(productId))
                .fetchOne();
            return result != null ? result : new LikeStatusProjection(0, false);
        }

        BooleanExpression liked =
            JPAExpressions
                .selectOne()
                .from(wishProduct)
                .where(
                    wishProduct.product.id.eq(productId),
                    wishProduct.user.id.eq(userId)
                )
                .exists();

        LikeStatusProjection result = jpaQueryFactory
            .select(
                Projections.constructor(
                    LikeStatusProjection.class,
                    likeCount,
                    liked
                )
            )
            .from(wishProduct)
            .where(wishProduct.product.id.eq(productId))
            .fetchOne();
        return result != null ? result : new LikeStatusProjection(0, false);
    }
}
