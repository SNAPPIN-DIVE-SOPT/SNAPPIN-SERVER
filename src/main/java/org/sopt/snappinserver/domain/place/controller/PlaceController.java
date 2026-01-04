package org.sopt.snappinserver.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@RestController
public class PlaceController implements PlaceApi{

}
