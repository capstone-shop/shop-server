package com.capstone.shop.user.v1.controller;

import com.capstone.shop.user.v1.dto.*;
import com.capstone.shop.entity.User;
import com.capstone.shop.exception.ResourceNotFoundException;
import com.capstone.shop.security.CurrentUser;
import com.capstone.shop.security.UserPrincipal;
import com.capstone.shop.user.v1.repository.UserRepository;
import com.capstone.shop.user.v1.service.AuthServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
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
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getAuthProvider(),
                signUpRequest.getAddress(),
                signUpRequest.getPhone_number(), //이거 언더바 쓴 이유는 json이랑 통일하기 위해서
                signUpRequest.getProfileImages(),
                signUpRequest.getRole()
        );
        // 회원 가입 성공 API 리턴
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }
    //TODO :AdditionalInfo by OAuth2 signUp
    @PostMapping("/additional-info")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveAdditionalInfo(@Valid @RequestBody OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest, UserPrincipal userPrincipal){
        authService.saveAdditionalInfo(userPrincipal.getId(), oAuth2AdditionalInfoRequest);
        return ResponseEntity.ok(new ApiResponse(true, "AdditionalInfo saved successfully"));
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
