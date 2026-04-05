package org.sopt.snappinserver.domain.portfolio.service.dto.response;

public record GetPortfolioCardResultV2(
    Long id,
    String imageUrl,
    boolean isLiked,
    long likeCount
) {

}
