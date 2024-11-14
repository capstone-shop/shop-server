package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.category.UserWebCategoryResponse;
import java.util.List;

public interface UserWebCategoryService {

    List<UserWebCategoryResponse> getCategory();
    List<UserWebCategoryResponse> getCategory(long categoryId);
}
