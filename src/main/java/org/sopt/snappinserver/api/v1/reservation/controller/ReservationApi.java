package org.sopt.snappinserver.api.v1.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.sopt.snappinserver.api.v1.reservation.dto.request.CreateReservationReviewRequest;
import org.sopt.snappinserver.api.v1.reservation.dto.request.RequestPaymentReservationRequest;
import org.sopt.snappinserver.api.v1.reservation.dto.response.CancelReservationResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.CompleteReservationResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.ConfirmReservationResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.CreateReservationReviewResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.PayReservationResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.RefuseReservationResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.RequestPaymentReservationResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.ReservationDetailResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.ReservationListResponse;
import org.sopt.snappinserver.api.v1.reservation.dto.response.ReservationPriceResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatusTab;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull Long reservationId,

        @Schema(description = "리뷰 정보")
        @Valid @RequestBody CreateReservationReviewRequest request
    );

    @Operation(
        summary = "예약 목록 조회",
        description = "예약 탭에 해당하는 예약 목록을 조회합니다."
    )
    @GetMapping
    ApiResponseBody<ReservationListResponse, Void> getReservations(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 조회 탭", example = "CLIENT_OVERVIEW")
        @RequestParam @NotNull ReservationStatusTab tab
    );

    @Operation(
        summary = "예약상세/촬영내역 조회",
        description = "예약된 상품에 대하여 예약 상세 정보와 결제 정보를 조회합니다."
    )
    @GetMapping("/{reservationId}")
    ApiResponseBody<ReservationDetailResponse, Void> getReservationDetail(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull Long reservationId
    );

    @Operation(
        summary = "결제하기 (고객)",
        description = "고객에게 결제 요청된 예약에 대해 결제 완료 상태로 변경합니다."
    )
    @PatchMapping("/{reservationId}/pay")
    ApiResponseBody<PayReservationResponse, Void> updateReservationPayment(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull @Positive Long reservationId
    );

    @Operation(
        summary = "예약 취소 (고객)",
        description = "고객의 예약을 취소 상태로 변경합니다."
    )
    @PatchMapping("/{reservationId}/cancel")
    ApiResponseBody<CancelReservationResponse, Void> updateReservationCancel(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull @Positive Long reservationId
    );

    @Operation(
        summary = "촬영 완료 및 리뷰 요청하기 (작가)",
        description = "예약 확정 상태인 작가의 예약을 촬영 완료 상태로 변경합니다."
    )
    @PatchMapping("/{reservationId}/complete")
    ApiResponseBody<CompleteReservationResponse, Void> updateReservationComplete(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull @Positive Long reservationId
    );

    @Operation(
        summary = "예약 확정 (작가)",
        description = "결제 완료된 작가의 예약을 예약 확정 상태로 변경합니다."
    )
    @PatchMapping("/{reservationId}/confirm")
    ApiResponseBody<ConfirmReservationResponse, Void> updateReservationConfirm(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull @Positive Long reservationId
    );

    @Operation(
        summary = "예약 거절 (작가)",
        description = "작가에게 들어온 예약에 대해 예약 거절 상태로 변경합니다."
    )
    @PatchMapping("/{reservationId}/refuse")
    ApiResponseBody<RefuseReservationResponse, Void> updateReservationRefuse(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull @Positive Long reservationId
    );

    @Operation(
        summary = "결제 요청 (작가)",
        description = "작가에게 요청된 예약에 대해 결제 금액을 확정하고 고객에게 결제를 요청합니다."
    )
    @PatchMapping("/{reservationId}/request-payment")
    ApiResponseBody<RequestPaymentReservationResponse, Void> updateReservationRequestPayment(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 아이디", example = "1")
        @PathVariable @NotNull @Positive Long reservationId,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "결제 요청 목록",
            required = true,
            content = @Content(schema = @Schema(implementation = RequestPaymentReservationRequest.class))
        )
        @Valid
        @org.springframework.web.bind.annotation.RequestBody
        RequestPaymentReservationRequest request

    );

    @Operation(
        summary = "기본 촬영 비용 조회 API",
        description = "작가의 결제 요청 과정에서 상품의 기본 촬영 비용을 조회합니다."
    )
    @GetMapping("/{reservationId}/price")
    ApiResponseBody<ReservationPriceResponse, Void> getReservationPrice(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "예약 ID")
        @PathVariable @NotNull Long reservationId
    );
}
