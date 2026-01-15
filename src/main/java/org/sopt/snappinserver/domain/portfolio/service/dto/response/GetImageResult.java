package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;

public record GetImageResult(
    String imageUrl,
    int order
) {

    public static GetImageResult from(PortfolioPhoto portfolioPhoto) {
        return new GetImageResult(
            portfolioPhoto.getPhoto().getImageUrl(),
            portfolioPhoto.getDisplayOrder()
        );
    }

    public static GetImageResult of(String presignedUrl, int order) {
        return new GetImageResult(presignedUrl, order);
    }
}

