package com.capstone.shop.admin.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "관리자 API")
public class AdminController {
    private final CategoryServiceImpl categoryService;

    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공",
                    content = @Content(schema = @Schema(implementation = CategoryRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addcategory")
    public ResponseEntity<CategoryRequestDto> createCategory(@RequestBody CategoryRequestDto categoryRequestDto){
        String title = categoryRequestDto.getTitle();
        String parentTitle = categoryRequestDto.getParent();
        CategoryRequestDto createdCategory = categoryService.createCategory(title,parentTitle);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
}