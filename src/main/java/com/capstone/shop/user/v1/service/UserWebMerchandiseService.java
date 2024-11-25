package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.User;
import com.capstone.shop.security.CurrentUser;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.search.Filter;
import org.springframework.data.domain.Pageable;

public interface UserWebMerchandiseService {

    UserWebMerchandisePagination getMerchandise(String search, Pageable pageable, Filter filter);

    UserWebMerchandiseDetail getMerchandise(Long merchandiseId);

    boolean createMerchandise(UserWebMerchandiseRegister request);

    HomeMerchandiseList getHomeMerchandiseList();

    ApiResponse WishCount(Long id, @CurrentUser User user);

}
