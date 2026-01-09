package org.sopt.snappinserver.domain.product.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSchedule;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerScheduleRepository;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductClosedDatesUseCase;
import org.sopt.snappinserver.global.enums.WeekDay;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetProductClosedDatesService implements GetProductClosedDatesUseCase {

    private static final int OPEN_MONTH_RANGE = 6;

    private final ProductRepository productRepository;
    private final PhotographerScheduleRepository photographerScheduleRepository;

    @Override
    public ProductClosedDatesResult getProductClosedDates(Long productId, YearMonth yearMonth) {
        Product product = getProduct(productId);
        Photographer photographer = product.getPhotographer();

        if (!isWithinOpenRange(yearMonth)) {
            return ProductClosedDatesResult.of(List.of());
        }

        List<WeekDay> closedWeekDays = getClosedWeekDays(photographer);
        List<LocalDate> closedDates = calculateClosedDates(yearMonth, closedWeekDays);

        return ProductClosedDatesResult.of(closedDates);
    }

    private Product getProduct(Long productId) {
        return productRepository
            .findById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    private List<LocalDate> calculateClosedDates(
        YearMonth yearMonth,
        List<WeekDay> closedWeekDays
    ) {
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        return startOfMonth
            .datesUntil(endOfMonth.plusDays(1))
            .filter(date -> closedWeekDays.contains(WeekDay.from(date.getDayOfWeek())))
            .toList();
    }

    private List<WeekDay> getClosedWeekDays(Photographer photographer) {
        return photographerScheduleRepository
            .findAllByPhotographerAndDayOffTrue(photographer)
            .stream()
            .map(PhotographerSchedule::getWeekDay)
            .toList();
    }

    private boolean isWithinOpenRange(YearMonth targetMonth) {
        YearMonth startMonth = YearMonth.now();
        YearMonth endMonth = startMonth.plusMonths(OPEN_MONTH_RANGE);

        return !targetMonth.isBefore(startMonth) && !targetMonth.isAfter(endMonth);
    }
}
