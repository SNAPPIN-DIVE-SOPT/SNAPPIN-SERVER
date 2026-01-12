package org.sopt.snappinserver.api.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.sopt.snappinserver.api.reservation.dto.request.CreateReservationReviewRequest;
import org.sopt.snappinserver.api.reservation.dto.response.CreateReservationReviewResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "010 - Reservation", description = "예약 관련 API")
public interface ReservationApi {
    @Operation(
        summary = "리뷰 등록",
        description = "촬영 완료된 예약 상품에 대해 리뷰를 작성합니다."
    )
    @PostMapping("/{reservationId}/reviews")
    ApiResponseBody<CreateReservationReviewResponse, Void> createReview(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @PathVariable @NotNull Long reservationId,

        @Valid @RequestBody CreateReservationReviewRequest request
    );

}
