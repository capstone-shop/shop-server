package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryResponseDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryTreeResponseDto;
import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.dto.CreateApiResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AdminWebCategoryService {
    CreateApiResponse createCategory(Long userId, CategoryRequestDto categoryRequestDto);

    ApiResponse updateCategory(Long userId, CategoryRequestDto categoryRequestDto,Long id);

    ApiResponse deleteCategory(Long id);

    CategoryResponseDto getCategory(Long id);

    List<CategoryResponseDto> getAllCategories();

    CategoryTreeResponseDto getCategoriesByParent(Long parentId);
    List<CategoryResponseDto> getAllMainCategories();
}
