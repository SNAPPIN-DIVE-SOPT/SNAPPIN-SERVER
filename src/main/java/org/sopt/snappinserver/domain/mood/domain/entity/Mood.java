package org.sopt.snappinserver.domain.mood.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.sopt.snappinserver.domain.mood.domain.VectorConverter;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.mood.domain.exception.MoodErrorCode;
import org.sopt.snappinserver.domain.mood.domain.exception.MoodException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Mood extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 10;
    private static final int EMBEDDING_DIMENSION = 512;
    private static final int MAX_DEFINITION_LENGTH = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mood_seq_gen")
    @SequenceGenerator(
        name = "mood_seq_gen",
        sequenceName = "mood_seq",
        allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MoodCategory category;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false, length = MAX_DEFINITION_LENGTH)
    private String definition;

    @Convert(converter = VectorConverter.class)
    @Column(columnDefinition = "vector")
    private List<Float> embedding;

    @Builder(access = AccessLevel.PRIVATE)
    private Mood(MoodCategory category, String name, String definition, List<Float> embedding) {
        this.category = category;
        this.name = name;
        this.definition = definition;
        this.embedding = embedding;
    }

    public static Mood create(
        MoodCategory category,
        String name,
        String definition,
        List<Float> embedding
    ) {
        validateMood(category, name, definition);
        return Mood.builder()
            .category(category)
            .name(name)
            .definition(definition)
            .embedding(embedding)
            .build();
    }

    private static void validateMood(MoodCategory category, String name, String definition) {
        validateCategoryExists(category);
        validateName(name);
        validateDefinition(definition);
    }

    private static void validateCategoryExists(MoodCategory category) {
        if (category == null) {
            throw new MoodException(MoodErrorCode.CATEGORY_REQUIRED);
        }
    }

    private static void validateName(String name) {
        validateNameExists(name);
        validateNameLength(name);
    }

    private static void validateNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new MoodException(MoodErrorCode.NAME_REQUIRED);
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new MoodException(MoodErrorCode.NAME_TOO_LONG);
        }
    }

    private static void validateDefinition(String definition) {
        validateDefinitionExists(definition);
        validateDefinitionLength(definition);
    }

    private static void validateDefinitionExists(String definition) {
        if (definition == null || definition.isBlank()) {
            throw new MoodException(MoodErrorCode.DEFINITION_REQUIRED);
        }
    }

    private static void validateDefinitionLength(String definition) {
        if (definition.length() > MAX_DEFINITION_LENGTH) {
            throw new MoodException(MoodErrorCode.DEFINITION_TOO_LONG);
        }
    }

}
