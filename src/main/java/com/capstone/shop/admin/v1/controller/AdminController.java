package com.capstone.shop.admin.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.capstone.shop.user.v1.dto.ApiResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "관리자 API")
public class AdminController {
    private final CategoryServiceImpl categoryService;

    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addcategory")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryRequestDto categoryRequestDto){


        ApiResponse response = categoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}