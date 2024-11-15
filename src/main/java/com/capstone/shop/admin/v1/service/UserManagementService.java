package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.dto.SignUpRequest;

public interface UserManagementService {
    ApiResponse createUser(SignUpRequest signUpRequest);

    ApiResponse updateUser(SignUpRequest signUpRequest);

    UserResponseDto getUser(Long id);

    PaginationResponse getAllUsers(int page, int size, String sort);

    void deleteUser(Long id);

}
