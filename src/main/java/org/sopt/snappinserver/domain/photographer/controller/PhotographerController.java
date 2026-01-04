package org.sopt.snappinserver.domain.photographer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/photographers")
@RequiredArgsConstructor
@RestController
public class PhotographerController implements PhotographerApi{

}
