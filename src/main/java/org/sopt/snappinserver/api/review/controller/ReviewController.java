package org.sopt.snappinserver.api.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController implements ReviewApi {

}
