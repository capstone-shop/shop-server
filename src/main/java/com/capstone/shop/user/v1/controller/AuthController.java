package com.capstone.shop.user.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.core.domain.dto.AdminSignUpRequest;
import com.capstone.shop.core.domain.dto.UserMeResponse;
import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.user.v1.controller.dto.auth.AccessTokenResponse;
import com.capstone.shop.user.v1.controller.dto.auth.OAuth2AdditionalInfoRequest;
import com.capstone.shop.user.v1.controller.dto.auth.SignInRequest;
import com.capstone.shop.core.exception.ResourceNotFoundException;
import com.capstone.shop.core.security.CurrentUser;
import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.user.v1.controller.dto.auth.SignUpRequest;
import com.capstone.shop.user.v1.service.AuthServiceImpl;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Map<String, String> tokens = authService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        return ResponseEntity.ok(new AccessTokenResponse(accessToken, refreshToken));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUpUser(
                signUpRequest
        );
        // 회원 가입 성공 API 리턴
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 수정 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdminSignUpRequest.class))
            )
            @RequestBody SignUpRequest signUpRequest, @CurrentUser UserPrincipal userPrincipal
    ) {
        // id로 사용자 존재 여부 확인
        UserMeResponse existingUser = authService.getMyInfo(userPrincipal.getId());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "사용자를 찾을 수 없습니다."));
        }

        // 사용자 업데이트 로직 실행
        ApiResponse response = authService.updateMyInfo(signUpRequest, userPrincipal.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/additional-info")
    @PreAuthorize("hasRole('PREUSER')") // 추가정보 입력받는 API
    public ResponseEntity<?> saveAdditionalInfo(@Valid @RequestBody OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest,@CurrentUser UserPrincipal userPrincipal){
        authService.saveAdditionalInfo(userPrincipal.getId(), oAuth2AdditionalInfoRequest);
        return ResponseEntity.ok(new ApiResponse(true, "AdditionalInfo saved successfully"));
    }



    @GetMapping("/me")
    public UserMeResponse getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .map(UserMeResponse::new) // UserMeResponse 생성자를 사용
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }


    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestParam Long Id,
                                                      @RequestParam String currentPassword,
                                                      @RequestParam String newPassword) {
        try {
            authService.changePassword(Id, currentPassword, newPassword);
            return ResponseEntity.ok(new ApiResponse(true, "비밀번호가 성공적으로 변경되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
