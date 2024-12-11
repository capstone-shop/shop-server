package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import org.springframework.data.domain.Pageable;

public interface UserWebMyInfoService {
    UserWebMerchandisePagination getWishlist(Long id, Pageable page);

    UserWebMerchandisePagination getRegisteredMerchandise(Long id, Pageable page);
}
