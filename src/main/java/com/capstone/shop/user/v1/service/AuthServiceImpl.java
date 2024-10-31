package com.capstone.shop.user.v1.service;

import com.capstone.shop.config.AppProperties;
import com.capstone.shop.exception.ResourceNotFoundException;

import com.capstone.shop.security.UserPrincipal;
import com.capstone.shop.user.v1.dto.OAuth2AdditionalInfoRequest;
import com.capstone.shop.user.v1.dto.SignUpRequest;
import com.capstone.shop.enums.AuthProvider;
import com.capstone.shop.enums.Role;
import com.capstone.shop.entity.User;
import com.capstone.shop.exception.BadRequestException;
import com.capstone.shop.security.TokenProvider;
import com.capstone.shop.user.v1.repository.UserRefreshTokenRepository;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
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
        long expiry = appProperties.getAuth().getTokenExpirationMsec();
        long refreshExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        Date now = new Date();
        Date accessTokenExpiryDate = new Date(now.getTime() + expiry);
        Date refreshTokenExpiryDate = new Date(now.getTime() + refreshExpiry);


        String accessToken = tokenProvider.createToken(authentication, accessTokenExpiryDate);
        String refreshToken = tokenProvider.createRefreshToken(authentication, refreshTokenExpiryDate);
        refreshTokenService.saveRefreshToken(user, refreshToken);
        Map<String, String> tokens = new HashMap<>();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }
    @Override
    public SignUpRequest signUpUser(String name, String email, String password, AuthProvider authProvider, String address, String phoneNumber, String profileImages, Role role) {
        if(userRepository.existsByEmail(email)){
            throw new BadRequestException("Email address already Exist.");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .address(address) // 주소 필드 추가
                .phoneNumber(phoneNumber) // 전화번호 필드 추가
                .authProvider(authProvider) // authProvider 필드 추가
                .profileImages(profileImages) // 프로필 이미지 필드 추가
                .role(role) // 역할 필드 추가
                .build();
        User savedUser = userRepository.save(user);

        return SignUpRequest.fromEntity(savedUser);

    }


    @Override
    public void saveAdditionalInfo(Long userId, OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setAddress(oAuth2AdditionalInfoRequest.getAddress());
        user.setPhoneNumber(oAuth2AdditionalInfoRequest.getPhoneNumber());

        userRepository.save(user);
    }
}