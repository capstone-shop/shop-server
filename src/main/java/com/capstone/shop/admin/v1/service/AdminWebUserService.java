package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.PaginatedResponse;
import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.dto.SignUpRequest;

public interface AdminWebUserService {
    ApiResponse createUser(SignUpRequest signUpRequest);

    ApiResponse updateUser(SignUpRequest signUpRequest,Long id);

    UserResponseDto getUser(Long id);

    PaginatedResponse<UserResponseDto> getAllUsers(int page, int size, String sort, String search);

    void deleteUser(Long id);

}
