package org.sopt.snappinserver.domain.auth.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {

    KAKAO("KAKAO"),
    GOOGLE("GOOGLE")
    ;

    private final String provider;
}
