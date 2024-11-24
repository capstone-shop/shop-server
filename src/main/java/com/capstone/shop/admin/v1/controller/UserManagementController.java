package com.capstone.shop.admin.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.PaginatedResponse;
import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.admin.v1.service.UserManagementService;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.dto.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin User Management", description = "관리자 유저 관리 API")
public class UserManagementController {

    private final UserManagementService userManagementService;

    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "사용자 생성 요청 데이터", required = true, content = @Content(schema = @Schema(implementation = SignUpRequest.class)))
            @RequestBody SignUpRequest signUpRequest) {
        ApiResponse response = userManagementService.createUser(signUpRequest);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "사용자 수정",
            description = "기존 사용자를 수정합니다."
    )
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 수정 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignUpRequest.class))
            )
            @RequestBody SignUpRequest signUpRequest,
            @PathVariable Long id
    ) {
        // id로 사용자 존재 여부 확인
        UserResponseDto existingUser = userManagementService.getUser(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "사용자를 찾을 수 없습니다."));
        }

        // 사용자 업데이트 로직 실행
        ApiResponse response = userManagementService.updateUser(signUpRequest, id);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "특정 사용자 조회", description = "ID로 특정 사용자를 조회합니다.")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "조회할 사용자 ID", required = true) @PathVariable Long id) {
        UserResponseDto user = userManagementService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "모든 사용자 조회", description = "모든 사용자를 페이징 처리하여 조회합니다.")
    @GetMapping
    public PaginatedResponse<UserResponseDto> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(required = false) String search // 검색어는 선택적으로
    ) {
        return userManagementService.getAllUsers(page, size, sort, search);
    }

    @Operation(summary = "사용자 삭제", description = "ID로 특정 사용자를 삭제합니다.")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(
            @Parameter(description = "삭제할 사용자 ID", required = true) @PathVariable Long id) {
        userManagementService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
    }
}
