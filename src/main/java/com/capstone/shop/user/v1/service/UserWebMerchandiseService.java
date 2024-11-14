package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseListAndPaginationResponse;
import com.capstone.shop.user.v1.util.Filter;
import org.springframework.data.domain.Pageable;

public interface UserWebMerchandiseService {

    MerchandiseListAndPaginationResponse getMerchandise(String sort, String search, Pageable pageable, Filter filter);

    boolean createMerchandise(Merchandise entity);

    HomeMerchandiseList getHomeMerchandiseList();
}
