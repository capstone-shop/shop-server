package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.user.v1.dto.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    ApiResponse createCategory(CategoryRequestDto categoryRequestDto);

    ApiResponse updateCategory(CategoryRequestDto categoryRequestDto);


}
