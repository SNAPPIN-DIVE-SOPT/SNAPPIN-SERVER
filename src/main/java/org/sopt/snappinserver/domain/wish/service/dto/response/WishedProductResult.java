package org.sopt.snappinserver.domain.wish.service.dto.response;

import java.util.List;

public record WishedProductResult(
    Long id,
    String imageUrl,
    String title,
    Double rate,
    Integer reviewCount,
    String photographer,
    Integer price,
    List<String> moods
) {

    public static WishedProductResult of(
        Long id,
        String imageUrl,
        String title,
        Double rate,
        Integer reviewCount,
        String photographer,
        Integer price,
        List<String> moods
    ) {
        return new WishedProductResult(
            id, imageUrl, title, rate, reviewCount, photographer, price, moods
        );
    }
}
