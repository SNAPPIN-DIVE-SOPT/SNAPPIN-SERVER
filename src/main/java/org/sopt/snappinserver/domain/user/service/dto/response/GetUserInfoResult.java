package org.sopt.snappinserver.domain.user.service.dto.response;

import org.sopt.snappinserver.domain.user.domain.entity.User;

public record GetUserInfoResult(
    Long id,
    String role,
    String profileImageUrl,
    boolean hasPhotographerProfile,
    GetClientInfoResult clientInfo,
    GetPhotographerInfoResult photographerInfo
) {

    public static GetUserInfoResult of(
        User user,
        String profileImageUrl,
        boolean hasPhotographerProfile,
        GetClientInfoResult clientInfo,
        GetPhotographerInfoResult photographerInfo
    ) {
        return new GetUserInfoResult(
            user.getId(),
            user.getRole().name(),
            profileImageUrl,
            hasPhotographerProfile,
            clientInfo,
            photographerInfo
        );
    }
}

