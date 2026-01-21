package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;
import java.util.Map;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.place.domain.entity.AvailableLocation;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductAvailableLocation;
import org.sopt.snappinserver.domain.product.domain.entity.ProductMood;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;

public record GetProductInfoResult(
    String snapCategory,
    List<String> regions,
    List<String> moods,
    String maxPeople,
    String photographerCount,
    double durationTime,
    String provideRaw,
    String provideOriginalJpg,
    String originalJpgCount,
    String originalDeliveryTime,
    String provideVideo,
    String freeRevisionCount,
    String finalCutCount,
    String finalDeliveryTime,
    String description,
    String processDescription,
    String equipment,
    String caution
) {

    public static GetProductInfoResult of(
        Product product,
        List<ProductAvailableLocation> productAvailableLocations,
        List<ProductMood> productMoods,
        Map<ProductOptionCategory, String> options
    ) {
        return new GetProductInfoResult(
            product.getSnapCategory().getCategory(),
            productAvailableLocations.stream()
                .map(ProductAvailableLocation::getAvailableLocation)
                .map(AvailableLocation::getFullLocation)
                .toList(),
            productMoods.stream()
                .map(ProductMood::getMood)
                .map(Mood::getName)
                .toList(),
            options.get(ProductOptionCategory.MAX_PEOPLE),
            options.get(ProductOptionCategory.PHOTOGRAPHER_COUNT),
            Math.round(Double.parseDouble(
                options.get(ProductOptionCategory.DURATION_TIME)) / 60d * 10
            ) / 10d,
            options.get(ProductOptionCategory.PROVIDE_RAW),
            options.get(ProductOptionCategory.PROVIDE_ORIGINAL_JPG),
            options.get(ProductOptionCategory.ORIGINAL_JPG_COUNT),
            options.get(ProductOptionCategory.ORIGINAL_DELIVERY_TIME),
            options.get(ProductOptionCategory.PROVIDE_VIDEO),
            options.get(ProductOptionCategory.FREE_REVISION_COUNT),
            options.get(ProductOptionCategory.FINAL_CUT_COUNT),
            options.get(ProductOptionCategory.FINAL_DELIVERY_TIME),
            product.getDescription(),
            product.getProcessDescription(),
            product.getEquipment(),
            product.getCaution()
        );
    }
}
