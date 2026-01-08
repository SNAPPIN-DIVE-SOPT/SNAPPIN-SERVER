package org.sopt.snappinserver.domain.product.service.dto.response;

public record ProductPeopleRangeResult(
    int minPeople,
    int maxPeople
) {}