package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;

public record GetPortfolioDetailResult(
    Long id,
    String description,
    List<String> images,
    boolean isLiked,
    int likeCount,
    String snapCategory,
    String place,
    String startsAt,
    List<String> moods,
    GetPhotographerInfoResult photographerInfo,
    GetProductInfoResult productInfo
) {

}
