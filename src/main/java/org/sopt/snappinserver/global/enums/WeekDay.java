package org.sopt.snappinserver.global.enums;

import java.time.DayOfWeek;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeekDay {

    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일"),
    ;

    public static WeekDay from(DayOfWeek dayOfWeek) {
        return WeekDay.valueOf(dayOfWeek.name());
    }

    private final String day;
}
