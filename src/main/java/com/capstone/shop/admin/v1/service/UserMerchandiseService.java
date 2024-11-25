package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.MerchandiseUpdateRequestDto;
import com.capstone.shop.user.v1.dto.ApiResponse;

public interface UserMerchandiseService {
    ApiResponse updateMerchandise(
            MerchandiseUpdateRequestDto merchandiseUpdateRequestDto, Long id);

    ApiResponse deleteMerchandise(Long id);
}
