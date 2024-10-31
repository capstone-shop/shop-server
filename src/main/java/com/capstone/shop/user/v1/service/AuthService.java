package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.dto.OAuth2AdditionalInfoRequest;
import com.capstone.shop.user.v1.dto.SignUpRequest;
import com.capstone.shop.enums.AuthProvider;
import com.capstone.shop.enums.Role;

import java.util.Map;

public interface AuthService {
    Map<String, String> signIn(String email, String password);
    SignUpRequest signUpUser(String name, String email, String password, AuthProvider authProvider, String address, String phoneNumber, String profileImages, Role role);

    void saveAdditionalInfo(Long userId, OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest);
}
