package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.category.UserWebCategory;
import java.util.List;

public interface UserWebCategoryService {

    List<UserWebCategory> getCategory();
    List<UserWebCategory> getCategory(long categoryId);
}
