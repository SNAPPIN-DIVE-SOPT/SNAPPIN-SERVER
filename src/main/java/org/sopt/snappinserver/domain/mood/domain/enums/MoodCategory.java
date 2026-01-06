package org.sopt.snappinserver.domain.mood.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MoodCategory {

    COMPOSITION("장면구성"),
    ATMOSPHERE("분위기"),
    STYLE("스타일"),
    ;

    private final String category;
}
