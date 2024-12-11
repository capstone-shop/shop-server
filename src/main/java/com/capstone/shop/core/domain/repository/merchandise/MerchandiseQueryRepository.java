package com.capstone.shop.core.domain.repository.merchandise;

import com.capstone.shop.core.domain.entity.Merchandise;

import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MerchandiseQueryRepository {
    List<Merchandise> findRelatedMerchandises(Merchandise merchandise);

    UserWebMerchandisePagination findWishlistOrderByWishDate(Long id, Pageable page);

    UserWebMerchandisePagination findRegisteredMerchandiseOrderByCreatedAt(Long id, Pageable page);
}
