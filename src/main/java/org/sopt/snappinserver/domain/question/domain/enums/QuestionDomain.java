package org.sopt.snappinserver.domain.question.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionDomain {

    MOOD_CURATION("무드 큐레이션"),
    SIGN_UP("자체 회원가입"),
    ;

    private final String domain;
}
