package org.sopt.snappinserver.api.v1.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.reservation.dto.response.ReservationPriceResponse;
import org.sopt.snappinserver.domain.reservation.service.dto.response.ReservationPriceResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationPriceUseCase;
import org.sopt.snappinserver.global.response.code.reservation.ReservationSuccessCode;
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
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatusTab;
import org.sopt.snappinserver.domain.reservation.service.dto.request.CreateReservationReviewCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.request.RequestPaymentReservationCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CreateReservationReviewResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationDetailUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationListUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationCancelUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationCompleteUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationConfirmUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationPayUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationRefuseUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationRequestPaymentUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PostReservationReviewUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@RestController
public class ReservationController implements ReservationApi {

    private final PostReservationReviewUseCase postReservationReviewUseCase;
    private final GetReservationListUseCase getReservationListUseCase;
    private final GetReservationDetailUseCase getReservationDetailUseCase;
    private final PatchReservationPayUseCase patchReservationPayUseCase;
    private final PatchReservationCancelUseCase patchReservationCancelUseCase;
    private final PatchReservationCompleteUseCase patchReservationCompleteUseCase;
    private final PatchReservationConfirmUseCase patchReservationConfirmUseCase;
    private final PatchReservationRefuseUseCase patchReservationRefuseUseCase;
    private final PatchReservationRequestPaymentUseCase patchReservationRequestPaymentUseCase;
    private final GetReservationPriceUseCase getReservationPriceUseCase;

    @Override
    public ApiResponseBody<CreateReservationReviewResponse, Void> createReview(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId,
        CreateReservationReviewRequest request
    ) {
        CreateReservationReviewCommand command = new CreateReservationReviewCommand(
            userInfo.userId(),
            reservationId,
            request.rating(),
            request.content(),
            request.imageUrls()
        );

        CreateReservationReviewResult result =
            postReservationReviewUseCase.createReservationReview(command);

        return ApiResponseBody.ok(
            ReservationSuccessCode.POST_RESERVATION_REVIEW_CREATED,
            CreateReservationReviewResponse.from(result)
        );
    }

    @Override
    public ApiResponseBody<ReservationListResponse, Void> getReservations(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        ReservationStatusTab tab
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.GET_RESERVATION_LIST_OK,
            ReservationListResponse.from(
                getReservationListUseCase.getReservationList(userInfo.userId(), tab)
            )
        );
    }

    @Override
    public ApiResponseBody<ReservationDetailResponse, Void> getReservationDetail(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.GET_RESERVATION_DETAIL_OK,
            ReservationDetailResponse.from(
                getReservationDetailUseCase.getReservationDetail(
                    userInfo.userId(),
                    reservationId
                )
            )
        );
    }

    @Override
    public ApiResponseBody<PayReservationResponse, Void> updateReservationPayment(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.PATCH_RESERVATION_PAY_OK,
            PayReservationResponse.from(
                patchReservationPayUseCase.payReservation(
                    userInfo.userId(),
                    reservationId
                )
            )
        );
    }

    @Override
    public ApiResponseBody<CancelReservationResponse, Void> updateReservationCancel(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.PATCH_RESERVATION_CANCEL_OK,
            CancelReservationResponse.from(
                patchReservationCancelUseCase.cancelReservation(
                    userInfo.userId(),
                    reservationId
                )
            )
        );
    }

    @Override
    public ApiResponseBody<CompleteReservationResponse, Void> updateReservationComplete(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.PATCH_RESERVATION_COMPLETE_OK,
            CompleteReservationResponse.from(
                patchReservationCompleteUseCase.completeReservation(
                    userInfo.userId(),
                    reservationId
                )
            )
        );
    }

    @Override
    public ApiResponseBody<ConfirmReservationResponse, Void> updateReservationConfirm(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.PATCH_RESERVATION_CONFIRM_OK,
            ConfirmReservationResponse.from(
                patchReservationConfirmUseCase.confirmReservation(
                    userInfo.userId(),
                    reservationId
                )
            )
        );
    }

    @Override
    public ApiResponseBody<RefuseReservationResponse, Void> updateReservationRefuse(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        return ApiResponseBody.ok(
            ReservationSuccessCode.PATCH_RESERVATION_REFUSE_OK,
            RefuseReservationResponse.from(
                patchReservationRefuseUseCase.refuseReservation(
                    userInfo.userId(),
                    reservationId
                )
            )
        );
    }

    @Override
    public ApiResponseBody<RequestPaymentReservationResponse, Void> updateReservationRequestPayment(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId,
        RequestPaymentReservationRequest request
    ) {
        RequestPaymentReservationCommand command = RequestPaymentReservationCommand.from(
            userInfo.userId(),
            reservationId,
            request
        );

        return ApiResponseBody.ok(
            ReservationSuccessCode.PATCH_RESERVATION_REQUEST_PAYMENT_OK,
            RequestPaymentReservationResponse.from(
                patchReservationRequestPaymentUseCase.requestPaymentReservation(command)
            )
        );
    }

    @Override
    public ApiResponseBody<ReservationPriceResponse, Void> getReservationPrice(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long reservationId
    ) {
        ReservationPriceResult result = getReservationPriceUseCase.getReservationPrice(reservationId);

        return ApiResponseBody.ok(
            ReservationSuccessCode.GET_RESERVATION_PRICE_OK,
            ReservationPriceResponse.from(result)
        );
    }


}
