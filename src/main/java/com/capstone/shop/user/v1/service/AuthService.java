package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.auth.OAuth2AdditionalInfoRequest;

import com.capstone.shop.core.domain.enums.AuthProvider;

import com.capstone.shop.core.domain.enums.Role;
import com.capstone.shop.user.v1.controller.dto.auth.SignUpRequest;

import java.util.Map;

public interface AuthService {
    Map<String, String> signIn(String email, String password);
    SignUpRequest signUpUser(SignUpRequest signUpRequest);

    void saveAdditionalInfo(Long userId, OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest);

    void changePassword(Long id, String CurrentPassword, String newPassword);
}
