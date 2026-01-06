package org.sopt.snappinserver.global.response.code.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

    // 200 OK

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
