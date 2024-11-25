package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.MerchandiseUpdateResponseDto;
import com.capstone.shop.user.v1.dto.ApiResponse;

public interface UserMerchandiseService {
    ApiResponse updateMerchandise(
            MerchandiseUpdateResponseDto merchandiseUpdateResponseDto, Long id);

    ApiResponse deleteMerchandise(Long id);
}
