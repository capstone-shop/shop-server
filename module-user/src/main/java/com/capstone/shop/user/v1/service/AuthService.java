package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.dto.UserMeResponse;
import com.capstone.shop.user.v1.controller.dto.auth.OAuth2AdditionalInfoRequest;
import com.capstone.shop.user.v1.controller.dto.auth.SignUpRequest;
import com.capstone.shop.user.v1.controller.dto.auth.UpdateUserRequest;
import java.util.Map;

public interface AuthService {
    Map<String, String> signIn(String email, String password);
    ApiResponse signUpUser(SignUpRequest signUpRequest);
    ApiResponse updateMyInfo(UpdateUserRequest updateUserRequest, Long id);

    UserMeResponse getMyInfo(Long id);
    void saveAdditionalInfo(Long userId, OAuth2AdditionalInfoRequest oAuth2AdditionalInfoRequest);

    void changePassword(Long id, String CurrentPassword, String newPassword);
}
