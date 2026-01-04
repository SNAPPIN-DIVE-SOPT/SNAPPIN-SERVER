package org.sopt.snappinserver.domain.photo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@RestController
public class PhotoController implements PhotoApi {

}
