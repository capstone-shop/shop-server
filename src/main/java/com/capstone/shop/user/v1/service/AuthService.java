package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.auth.OAuth2AdditionalInfoRequest;
import com.capstone.shop.core.domain.dto.SignUpRequest;

import com.capstone.shop.core.domain.enums.AuthProvider;

import com.capstone.shop.core.domain.enums.Role;

import java.util.Map;

public interface AuthService {
    Map<String, String> signIn(String email, String password);
    SignUpRequest signUpUser(String name, String email, String password, AuthProvider authProvider, String address, String phoneNumber, String profileImages, Role role);

    void saveAdditionalInfo(Long userId, OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest);

    void changePassword(Long id, String CurrentPassword, String newPassword);
}
