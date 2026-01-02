package org.sopt.snappinserver.domain.product.controller.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {

    // 200 OK

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;

}
