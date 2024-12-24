package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.PaginatedResponse;
import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.core.domain.dto.AdminSignUpRequest;
import com.capstone.shop.core.domain.dto.ApiResponse;

public interface AdminWebUserService {
    ApiResponse createUser(AdminSignUpRequest adminSignUpRequest);

    ApiResponse updateUser(AdminSignUpRequest adminSignUpRequest,Long id);

    UserResponseDto getUser(Long id);

    PaginatedResponse<UserResponseDto> getAllUsers(int page, int size, String sort, String search);

    void deleteUser(Long id);

}
