package org.sopt.snappinserver.domain.user.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;

public record GetClientInfoResult(
    String name,
    String nickname,
    List<String> curatedMoods
) {

    public static GetClientInfoResult create(Onboarding onboarding, List<String> curatedMoods) {
        return new GetClientInfoResult(
            onboarding.getName(),
            onboarding.getNickname(),
            curatedMoods
        );
    }
}
