package com.capstone.shop.user.v1.controller;

import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.service.UserWebMerchandiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class HomeController {

    private final UserWebMerchandiseService userWebMerchandiseService;

    @GetMapping("/merchandise")
    public HomeMerchandiseList getMerchandise() {
        return userWebMerchandiseService.getHomeMerchandiseList();
    }
}
