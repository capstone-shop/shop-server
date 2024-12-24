package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.dto.PaginationQueryResult;
import com.capstone.shop.core.domain.entity.Merchandise;
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
        PaginationQueryResult<Merchandise> result = merchandiseQueryRepository.findWishlistOrderByWishDate(id, page);
        return new UserWebMerchandisePagination(result);
    }

    @Override
    public UserWebMerchandisePagination getRegisteredMerchandise(Long id, Pageable page) {
        PaginationQueryResult<Merchandise> result = merchandiseQueryRepository.findRegisteredMerchandiseOrderByCreatedAt(id, page);
        return new UserWebMerchandisePagination(result);
    }
}
