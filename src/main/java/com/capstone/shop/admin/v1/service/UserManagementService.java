package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.PaginatedResponse;
import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.dto.SignUpRequest;

public interface UserManagementService {
    ApiResponse createUser(SignUpRequest signUpRequest);

    ApiResponse updateUser(SignUpRequest signUpRequest);

    UserResponseDto getUser(Long id);

    PaginatedResponse<UserResponseDto> getAllUsers(int page, int size, String sort, String search);

    void deleteUser(Long id);

}
