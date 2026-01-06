package org.sopt.snappinserver.api.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@RestController
public class ReservationController implements ReservationApi {

}
