package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.dto.UserMeResponse;
import com.capstone.shop.core.exception.ResourceNotFoundException;

import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.user.v1.controller.dto.auth.OAuth2AdditionalInfoRequest;

import com.capstone.shop.core.domain.enums.AuthProvider;
import com.capstone.shop.core.domain.enums.Role;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.exception.BadRequestException;
import com.capstone.shop.core.security.TokenProvider;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.user.v1.controller.dto.auth.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
//commit
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Map<String, String> signIn(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));
        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        refreshTokenService.saveRefreshToken(user, refreshToken);
        Map<String, String> tokens = new HashMap<>();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }
    @Override
    public SignUpRequest signUpUser(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new BadRequestException("Email address already Exist.");
        }

        User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .address(signUpRequest.getAddress()) // 주소 필드 추가
                .phoneNumber(signUpRequest.getPhone_number()) // 전화번호 필드 추가
                .authProvider(AuthProvider.local) // authProvider 필드 추가
                .profileImages(signUpRequest.getProfileImages()) // 프로필 이미지 필드 추가
                .role(Role.ROLE_USER) // 역할 필드 추가
                .build();
        User savedUser = userRepository.save(user);

        return SignUpRequest.fromEntity(savedUser);

    }

    @Override
    public ApiResponse updateMyInfo(SignUpRequest signUpRequest,Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("내정보 찾기 불가"));

        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setAddress(signUpRequest.getAddress());
        user.setPhoneNumber(signUpRequest.getPhone_number()); // JSON 필드명 통일
        user.setProfileImages(signUpRequest.getProfileImages());

        userRepository.save(user);

        return new ApiResponse(true, "유저 업데이트 성공");
    }

    @Override
    public UserMeResponse getMyInfo(Long id) {
        User myInfo = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("내 정보를 찾을 수 없음"));
        return new UserMeResponse(myInfo);
    }


    @Override
    public void saveAdditionalInfo(Long userId, OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setAddress(oAuth2AdditionalInfoRequest.getAddress());
        user.setPhoneNumber(oAuth2AdditionalInfoRequest.getPhoneNumber());
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }

    @Override
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}