package com.capstone.shop.admin.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryResponseDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryTreeResponseDto;
import com.capstone.shop.admin.v1.service.CategoryServiceImpl;
import com.capstone.shop.user.v1.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "관리자 API")
public class CategoryAdminController {
    private final CategoryServiceImpl categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        ApiResponse response = categoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다.")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody CategoryRequestDto categoryRequestDto, @PathVariable Long id) {
        ApiResponse response = categoryService.updateCategory(categoryRequestDto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        ApiResponse response = categoryService.deleteCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{title}")
    @Operation(summary = "카테고리 조회", description = "카테고리 제목으로 카테고리를 조회합니다.")
    public ResponseEntity<CategoryResponseDto> getCategoryByTitle(@PathVariable String title) {
        CategoryResponseDto response = categoryService.getCategoryByTitle(title);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "전체 카테고리를 조회합니다.")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> response = categoryService.getAllCategories();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/parent/{parentTitle}")
    @Operation(summary = "하위 카테고리 조회", description = "부모 카테고리의 하위 카테고리를 조회합니다.")
    public ResponseEntity<CategoryTreeResponseDto> getCategoriesByParent(@PathVariable String parentTitle) {
        CategoryTreeResponseDto response = categoryService.getCategoriesByParent(parentTitle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
