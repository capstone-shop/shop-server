package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.*;
import com.capstone.shop.core.domain.dto.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminWebCategoryService {
    ApiResponse createCategory(Long userId, CategoryRequestDto categoryRequestDto);

    ApiResponse updateCategory(Long userId, CategoryRequestDto categoryRequestDto,Long id);

    ApiResponse deleteCategory(Long id);

    CategoryResponseDto getCategoryByTitle(String categoryTitle);

    List<CategoryResponseDto> getAllCategories();

    CategoryTreeResponseDto getCategoriesByParent(String parentTitle);
}
