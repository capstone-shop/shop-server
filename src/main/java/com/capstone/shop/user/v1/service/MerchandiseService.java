package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseListAndPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MerchandiseService {

    MerchandiseListAndPaginationResponse getMerchandise(String sort, String search, Pageable pageable);

    boolean createMerchandise(Merchandise entity);

    HomeMerchandiseList getRecentlyViewedAndRecentlyRegisteredMerchandise();
}
