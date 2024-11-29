package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.MerchandiseUpdateRequestDto;
import com.capstone.shop.core.domain.dto.ApiResponse;

public interface AdminWebMerchandiseService {
    ApiResponse updateMerchandise(
            MerchandiseUpdateRequestDto merchandiseUpdateRequestDto, Long id);

    ApiResponse deleteMerchandise(Long id);
}
