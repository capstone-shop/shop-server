package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.repository.merchandise.MerchandiseQueryRepository;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWebMyInfoServiceImpl implements UserWebMyInfoService {

    private final MerchandiseQueryRepository merchandiseQueryRepository;

    @Override
    public UserWebMerchandisePagination getWishlist(Long id, Pageable page) {
        return merchandiseQueryRepository.findWishlistOrderByWishDate(id, page);
    }

    @Override
    public UserWebMerchandisePagination getRegisteredMerchandise(Long id, Pageable page) {
        return merchandiseQueryRepository.findRegisteredMerchandiseOrderByCreatedAt(id, page);
    }
}
