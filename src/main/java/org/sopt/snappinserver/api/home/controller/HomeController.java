package org.sopt.snappinserver.api.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@RestController
public class HomeController implements HomeApi{

}
