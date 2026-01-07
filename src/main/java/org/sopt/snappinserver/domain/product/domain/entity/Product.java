package org.sopt.snappinserver.domain.product.domain.entity;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.global.entity.BaseEntity;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_PRICE = 1_000_000;
    private static final int MIN_PRICE = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 1024;
    private static final int MAX_EQUIPMENT_LENGTH = 512;
    private static final int MAX_PROCESS_DESCRIPTION_LENGTH = 1024;
    private static final int MAX_CAUTION_LENGTH = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    @Column(nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnapCategory snapCategory;

    @Column(length = MAX_DESCRIPTION_LENGTH)
    private String description;

    @Column(length = MAX_EQUIPMENT_LENGTH)
    private String equipment;

    @Column(length = MAX_PROCESS_DESCRIPTION_LENGTH)
    private String processDescription;

    @Column(length = MAX_CAUTION_LENGTH)
    private String caution;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private LocalDateTime startsAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private LocalDateTime endsAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Product(
        Photographer photographer,
        String title,
        Integer price,
        SnapCategory snapCategory,
        String description,
        String equipment,
        String processDescription,
        String caution,
        LocalDateTime startsAt,
        LocalDateTime endsAt
    ) {
        this.photographer = photographer;
        this.title = title;
        this.price = price;
        this.snapCategory = snapCategory;
        this.description = description;
        this.equipment = equipment;
        this.processDescription = processDescription;
        this.caution = caution;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public static Product create(
        Photographer photographer,
        String title,
        Integer price,
        SnapCategory snapCategory,
        String description,
        String equipment,
        String processDescription,
        String caution,
        LocalDateTime startsAt,
        LocalDateTime endsAt
    ) {
        validateProduct(
            photographer,
            title,
            price,
            snapCategory,
            description,
            equipment,
            processDescription,
            caution,
            startsAt,
            endsAt
        );
        return Product.builder()
            .photographer(photographer)
            .title(title)
            .price(price)
            .snapCategory(snapCategory)
            .description(description)
            .equipment(equipment)
            .processDescription(processDescription)
            .caution(caution)
            .startsAt(startsAt)
            .endsAt(endsAt)
            .build();
    }

    private static void validateProduct(
        Photographer photographer,
        String title,
        Integer price,
        SnapCategory snapCategory,
        String description,
        String equipment,
        String processDescription,
        String caution,
        LocalDateTime startsAt,
        LocalDateTime endsAt
    ) {
        validatePhotographerExists(photographer);
        validateTitle(title);
        validatePrice(price);
        validateSnapCategoryExists(snapCategory);
        validateDescriptionLength(description);
        validateEquipmentLength(equipment);
        validateProcessDescriptionLength(processDescription);
        validateCautionLength(caution);
        validateTime(startsAt, endsAt);
    }

    private static void validatePhotographerExists(Photographer photographer) {
        if (photographer == null) {
            throw new ProductException(ProductErrorCode.PHOTOGRAPHER_REQUIRED);
        }
    }

    private static void validateTitle(String title) {
        validateTitleExists(title);
        validateTitleLength(title);
    }

    private static void validateTitleExists(String title) {
        if (title == null || title.isBlank()) {
            throw new ProductException(ProductErrorCode.TITLE_REQUIRED);
        }
    }

    private static void validateTitleLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new ProductException(ProductErrorCode.TITLE_TOO_LONG);
        }
    }

    private static void validatePrice(Integer price) {
        validatePriceExists(price);
        validateMaxPrice(price);
        validateMinPrice(price);
    }

    private static void validatePriceExists(Integer price) {
        if (price == null) {
            throw new ProductException(ProductErrorCode.PRICE_REQUIRED);
        }
    }

    private static void validateMaxPrice(Integer price) {
        if (price > MAX_PRICE) {
            throw new ProductException(ProductErrorCode.PRICE_TOO_EXPENSIVE);
        }
    }

    private static void validateMinPrice(Integer price) {
        if (price < MIN_PRICE) {
            throw new ProductException(ProductErrorCode.PRICE_TOO_CHEAP);
        }
    }

    private static void validateSnapCategoryExists(SnapCategory snapCategory) {
        if (snapCategory == null) {
            throw new ProductException(ProductErrorCode.SNAP_CATEGORY_REQUIRED);
        }
    }

    private static void validateDescriptionLength(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new ProductException(ProductErrorCode.DESCRIPTION_TOO_LONG);
        }
    }

    private static void validateEquipmentLength(String equipment) {
        if (equipment != null && equipment.length() > MAX_EQUIPMENT_LENGTH) {
            throw new ProductException(ProductErrorCode.EQUIPMENT_TOO_LONG);
        }
    }

    private static void validateProcessDescriptionLength(String processDescription) {
        if (processDescription != null
            && processDescription.length() > MAX_PROCESS_DESCRIPTION_LENGTH) {
            throw new ProductException(ProductErrorCode.PROCESS_DESCRIPTION_TOO_LONG);
        }
    }

    private static void validateCautionLength(String caution) {
        if (caution != null && caution.length() > MAX_CAUTION_LENGTH) {
            throw new ProductException(ProductErrorCode.CAUTION_TOO_LONG);
        }
    }

    private static void validateTime(LocalDateTime starts_at, LocalDateTime ends_at) {
        validateStartsAtExists(starts_at);
        validateEndsAtExists(ends_at);
        validateTimeOrder(starts_at, ends_at);
    }

    private static void validateStartsAtExists(LocalDateTime starts_at) {
        if (starts_at == null) {
            throw new ProductException(ProductErrorCode.STARTS_AT_REQUIRED);
        }
    }

    private static void validateEndsAtExists(LocalDateTime ends_at) {
        if (ends_at == null) {
            throw new ProductException(ProductErrorCode.ENDS_AT_REQUIRED);
        }
    }

    private static void validateTimeOrder(LocalDateTime starts_at, LocalDateTime ends_at) {
        if (starts_at.isAfter(ends_at) || starts_at.isEqual(ends_at)) {
            throw new ProductException(ProductErrorCode.STARTS_AT_AFTER_ENDS_AT);
        }
    }
}
