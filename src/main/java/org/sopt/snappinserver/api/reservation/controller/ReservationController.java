package org.sopt.snappinserver.api.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.reservation.code.ReservationSuccessCode;
import org.sopt.snappinserver.api.reservation.dto.request.CreateReservationReviewRequest;
import org.sopt.snappinserver.api.reservation.dto.response.CreateReservationReviewResponse;
import org.sopt.snappinserver.api.reservation.dto.response.ReservationDetailResponse;
import org.sopt.snappinserver.api.reservation.dto.response.ReservationListResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatusTab;
import org.sopt.snappinserver.domain.reservation.service.dto.request.CreateReservationReviewCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CreateReservationReviewResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationDetailUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationListUseCase;
import org.sopt.snappinserver.domain.reservation.service.usecase.PostReservationReviewUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@RestController
public class ReservationController implements ReservationApi {

    private final PostReservationReviewUseCase postReservationReviewUseCase;
    private final GetReservationListUseCase getReservationListUseCase;
    private final GetReservationDetailUseCase getReservationDetailUseCase;

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
}
