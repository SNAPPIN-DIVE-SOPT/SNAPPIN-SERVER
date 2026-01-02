package org.sopt.snappinserver.domain.photo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.sopt.snappinserver.domain.photo.domain.exception.PhotoErrorCode;
import org.sopt.snappinserver.domain.photo.domain.exception.PhotoException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Photo extends BaseEntity {

    private static final int MAX_IMAGE_URL_LENGTH = 1024;
    private static final int EMBEDDING_DIMENSION = 512;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = MAX_IMAGE_URL_LENGTH)
    private String imageUrl;

    @Column(columnDefinition = "vector(" + EMBEDDING_DIMENSION + ")")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private List<Float> embedding;

    @Builder(access = AccessLevel.PRIVATE)
    private Photo(String imageUrl, List<Float> embedding) {
        this.imageUrl = imageUrl;
        this.embedding = embedding;
    }

    public static Photo create(String imageUrl, List<Float> embedding) {
        validatePhoto(imageUrl);
        return Photo.builder()
            .imageUrl(imageUrl)
            .embedding(embedding)
            .build();
    }

    private static void validatePhoto(String imageUrl) {
        validateImageUrlExists(imageUrl);
        validateImageUrlLength(imageUrl);
    }

    private static void validateImageUrlExists(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new PhotoException(PhotoErrorCode.IMAGE_URL_REQUIRED);
        }
    }

    private static void validateImageUrlLength(String imageUrl) {
        if (imageUrl.length() > MAX_IMAGE_URL_LENGTH) {
            throw new PhotoException(PhotoErrorCode.IMAGE_URL_TOO_LONG);
        }
    }

}
