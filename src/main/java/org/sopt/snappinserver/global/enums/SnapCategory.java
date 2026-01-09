package org.sopt.snappinserver.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnapCategory {

    GRADUATION("졸업스냅"),
    WEDDING("결혼스냅"),
    COUPLE("커플스냅"),
    DAILY("일상스냅"),
    FAMILY("가족스냅"),
    RECITAL("연주스냅"),
    ;

    private final String category;

}
