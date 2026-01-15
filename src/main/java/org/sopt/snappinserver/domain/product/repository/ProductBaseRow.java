package org.sopt.snappinserver.domain.product.repository;

public record ProductBaseRow(
    Long id,
    String imageUrl,
    String title,
    Double rate,
    Long reviewCount,
    String photographerName,
    Integer price
) {

}
