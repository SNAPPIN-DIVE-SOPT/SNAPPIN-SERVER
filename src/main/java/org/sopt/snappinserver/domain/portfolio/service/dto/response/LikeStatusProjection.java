package org.sopt.snappinserver.domain.portfolio.service.dto.response;

public record LikeStatusProjection(
    Integer likeCount,
    Boolean liked
) {

}
