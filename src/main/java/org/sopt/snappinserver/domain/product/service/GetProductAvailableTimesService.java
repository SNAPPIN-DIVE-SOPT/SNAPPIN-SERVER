package org.sopt.snappinserver.domain.product.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSchedule;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerErrorCode;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerException;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerScheduleRepository;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductOptionRepository;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimeResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductAvailableTimesUseCase;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.global.enums.WeekDay;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductAvailableTimesService implements GetProductAvailableTimesUseCase {

    private static final int ONE_DAY = 1;
    private static final int TIME_SLOT_INTERVAL_MINUTES = 30;

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final PhotographerScheduleRepository photographerScheduleRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ProductAvailableTimesResult getProductAvailableTimes(Long productId, LocalDate date) {

        Product product = getProduct(productId);
        Photographer photographer = product.getPhotographer();
        WeekDay weekDay = WeekDay.from(date.getDayOfWeek());
        PhotographerSchedule schedule = getPhotographerSchedule(photographer, weekDay);

        // 휴무일인 경우 예약 불가
        if (schedule.isDayOff()) {
            throw new ProductException(ProductErrorCode.PRODUCT_UNAVAILABLE_DATE);
        }

        int durationMinutes = getDurationMinutes(product);

        LocalTime workStart = schedule.getStartTime();
        LocalTime workEnd = schedule.getEndTime();
        LocalTime lastStartTime = workEnd.minusMinutes(durationMinutes);

        // 촬영 시간이 너무 길어 시작 가능한 시간이 없는 경우 예약 불가
        if (lastStartTime.isBefore(workStart)) {
            return new ProductAvailableTimesResult(date, List.of());
        }

        List<LocalTime> slots = createTimeSlots(workStart, lastStartTime);
        List<Reservation> reservations = getConfirmedReservations(date, product);
        List<ProductAvailableTimeResult> results = getProductAvailableTimeResults(
            slots,
            reservations,
            durationMinutes
        );

        return new ProductAvailableTimesResult(date, results);
    }

    // 상품 조회
    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    // 작가의 요일별 스케줄 조회
    private PhotographerSchedule getPhotographerSchedule(Photographer photographer,
        WeekDay weekDay) {
        return photographerScheduleRepository.findByPhotographerAndWeekDay(photographer, weekDay)
            .orElseThrow(() -> new PhotographerException(PhotographerErrorCode.SCHEDULE_NOT_FOUND));
    }

    // 상품 옵션에서 촬영 시간 조회
    private int getDurationMinutes(Product product) {
        ProductOption durationOption = productOptionRepository
            .findByProductAndProductOptionCategory(product, ProductOptionCategory.DURATION_TIME)
            .orElseThrow(() -> new ProductException(ProductErrorCode.DURATION_TIME_REQUIRED));

        return Integer.parseInt(durationOption.getAnswer());
    }

    // 업무 시간 기준 30분 단위의 시작 시간 슬롯 생성
    private List<LocalTime> createTimeSlots(LocalTime workStart, LocalTime lastStartTime) {
        List<LocalTime> slots = new ArrayList<>();

        for (LocalTime time = workStart; !time.isAfter(lastStartTime);
            time = time.plusMinutes(TIME_SLOT_INTERVAL_MINUTES)) {
            slots.add(time);
        }
        return slots;
    }

    // 시간 슬롯별 예약 가능 여부 계산
    private List<Reservation> getConfirmedReservations(LocalDate date, Product product) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(ONE_DAY).atStartOfDay();

        return reservationRepository.findAllByProductAndReservedAtBetweenAndReservationStatusIn(
            product,
            startOfDay,
            endOfDay,
            List.of(ReservationStatus.RESERVATION_CONFIRMED, ReservationStatus.SHOOT_COMPLETED)
        );
    }

    // 시간대별 예약 가능 여부 목록 생성
    private List<ProductAvailableTimeResult> getProductAvailableTimeResults(
        List<LocalTime> slots,
        List<Reservation> reservations,
        int durationMinutes
    ) {
        return slots.stream()
            .map(slot -> new ProductAvailableTimeResult(
                slot,
                isAvailableTimeSlot(slot, reservations, durationMinutes)
            ))
            .toList();
    }

    // 특정 시작 시간 슬롯이 예약 가능한지 판단
    private boolean isAvailableTimeSlot(
        LocalTime slot,
        List<Reservation> reservations,
        int durationMinutes
    ) {
        return reservations.stream().noneMatch(
            reservation -> isOverlapping(slot, reservation, durationMinutes)
        );
    }

    // 시간 슬롯과 예약 시간대가 겹치는지 판단
    private boolean isOverlapping(LocalTime slot, Reservation reservation, int durationMinutes) {
        LocalTime reservedStart = reservation.getReservedAt().toLocalTime();
        LocalTime reservedEnd = reservedStart.plusMinutes(reservation.getDurationTime());

        return slot.isBefore(reservedEnd) && slot.plusMinutes(durationMinutes)
            .isAfter(reservedStart);
    }
}
