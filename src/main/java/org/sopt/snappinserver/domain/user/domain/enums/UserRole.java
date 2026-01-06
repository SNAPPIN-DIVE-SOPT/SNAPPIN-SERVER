package org.sopt.snappinserver.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    CLIENT("고객"),
    PHOTOGRAPHER("작가")
    ;

    private final String role;

}
