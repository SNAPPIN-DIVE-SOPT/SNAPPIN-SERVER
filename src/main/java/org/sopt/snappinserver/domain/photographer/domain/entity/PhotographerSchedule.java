package org.sopt.snappinserver.domain.photographer.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerErrorCode;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerException;
import org.sopt.snappinserver.global.entity.BaseEntity;
import org.sopt.snappinserver.global.enums.WeekDay;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Check(constraints = "start_time < end_time")
public class PhotographerSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private WeekDay weekDay;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean dayOff;

    @Builder(access = AccessLevel.PRIVATE)
    private PhotographerSchedule(
        Photographer photographer,
        WeekDay weekDay,
        LocalTime startTime,
        LocalTime endTime,
        boolean dayOff
    ) {
        this.photographer = photographer;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOff = dayOff;
    }

    public static PhotographerSchedule create(
        Photographer photographer,
        WeekDay weekDay,
        LocalTime startTime,
        LocalTime endTime,
        boolean dayOff
    ) {
        validatePhotographerSchedule(photographer, weekDay, startTime, endTime, dayOff);
        return PhotographerSchedule.builder()
            .photographer(photographer)
            .weekDay(weekDay)
            .startTime(startTime)
            .endTime(endTime)
            .dayOff(dayOff)
            .build();
    }

    private static void validatePhotographerSchedule(
        Photographer photographer,
        WeekDay weekDay,
        LocalTime startTime,
        LocalTime endTime,
        boolean dayOff
    ) {
        validatePhotographerExists(photographer);
        validateWeekDayExists(weekDay);
        validateTime(dayOff, startTime, endTime);
    }

    private static void validatePhotographerExists(Photographer photographer) {
        if (photographer == null) {
            throw new PhotographerException(PhotographerErrorCode.PHOTOGRAPHER_REQUIRED);
        }
    }

    private static void validateWeekDayExists(WeekDay weekDay) {
        if (weekDay == null) {
            throw new PhotographerException(PhotographerErrorCode.WEEK_DAY_REQUIRED);
        }
    }

    private static void validateTime(boolean dayOff, LocalTime startTime, LocalTime endTime) {
        if (dayOff) {
            return;
        }
        validateStartTimeExists(startTime);
        validateEndTimeExists(endTime);
        validateTimeOrder(startTime, endTime);
    }

    private static void validateStartTimeExists(LocalTime startTime) {
        if (startTime == null) {
            throw new PhotographerException(PhotographerErrorCode.START_TIME_REQUIRED);
        }
    }

    private static void validateEndTimeExists(LocalTime endTime) {
        if (endTime == null) {
            throw new PhotographerException(PhotographerErrorCode.END_TIME_REQUIRED);
        }
    }

    private static void validateTimeOrder(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new PhotographerException(PhotographerErrorCode.START_TIME_AFTER_THAN_END_TIME);
        }
    }

}
