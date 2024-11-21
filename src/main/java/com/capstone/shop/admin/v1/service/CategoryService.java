package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.*;
import com.capstone.shop.user.v1.dto.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    ApiResponse createCategory(CategoryRequestDto categoryRequestDto);

    ApiResponse updateCategory(CategoryRequestDto categoryRequestDto);

    ApiResponse deleteCategory(String categortTitle);

    CategoryResponseDto getCategoryByTitle(String categoryTitle);

    List<CategoryResponseDto> getAllCategories();

    CategoryTreeResponseDto getCategoriesByParent(String parentTitle);
}
