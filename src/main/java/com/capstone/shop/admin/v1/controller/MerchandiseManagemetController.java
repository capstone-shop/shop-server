package com.capstone.shop.admin.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.MerchandiseUpdateRequestDto;
import com.capstone.shop.admin.v1.service.UserMerchandiseServiceImpl;
import com.capstone.shop.user.v1.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/merchandise")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "관리자 상품 관리 API")
public class MerchandiseManagemetController {
    private final UserMerchandiseServiceImpl userMerchandiseService;

    @PutMapping
    @Operation(summary = "상품정보 수정", description = "기존 상품정보를 수정합니다.")
    public ResponseEntity<ApiResponse> updateMerchandise(@RequestBody MerchandiseUpdateRequestDto merchandiseUpdateRequestDto, @PathVariable Long id) {
        ApiResponse response = userMerchandiseService.updateMerchandise(merchandiseUpdateRequestDto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품삭제", description = "상품을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteMerchandise(@PathVariable Long id) {
        ApiResponse response = userMerchandiseService.deleteMerchandise(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
