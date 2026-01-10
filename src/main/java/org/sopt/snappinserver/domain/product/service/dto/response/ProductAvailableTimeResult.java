package org.sopt.snappinserver.domain.product.service.dto.response;

import java.time.LocalTime;

public record ProductAvailableTimeResult(LocalTime time, boolean isAvailable) {

}
