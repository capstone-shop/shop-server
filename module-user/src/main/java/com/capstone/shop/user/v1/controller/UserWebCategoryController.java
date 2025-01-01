package com.capstone.shop.user.v1.controller;

import com.capstone.shop.user.v1.controller.dto.category.UserWebCategory;
import com.capstone.shop.user.v1.service.UserWebCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class UserWebCategoryController {

    private final UserWebCategoryService userWebCategoryService;

    @GetMapping
    public List<UserWebCategory> getCategory() {
        return userWebCategoryService.getCategory();
    }

    @GetMapping("/{category_id}/sub")
    public List<UserWebCategory> getCategory(@PathVariable String category_id) {
        long categoryId = Long.parseLong(category_id);
        return userWebCategoryService.getCategory(categoryId);
    }
}
