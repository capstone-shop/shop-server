package com.capstone.shop.user.v1.controller;

import com.capstone.shop.user.v1.controller.dto.category.UserWebCategoryResponse;
import com.capstone.shop.user.v1.service.UserWebCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class UserWebCategoryController {

    private final UserWebCategoryService userWebCategoryService;

    @GetMapping
    public List<UserWebCategoryResponse> getCategory() {
        return userWebCategoryService.getUserWebCategory();
    }
}
