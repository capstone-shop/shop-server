package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.Category;
import com.capstone.shop.user.v1.controller.dto.category.UserWebCategory;
import com.capstone.shop.user.v1.repository.UserWebCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWebCategoryServiceImpl implements UserWebCategoryService {

    private final UserWebCategoryRepository userWebCategoryRepository;

    @Override
    public List<UserWebCategory> getCategory() {
        List<Category> result = userWebCategoryRepository.findAllByParentOrderBySequenceAsc(null);
        return UserWebCategory.entityListToDtoList(result);
    }

    @Override
    public List<UserWebCategory> getCategory(long categoryId) {
        List<Category> result = userWebCategoryRepository.findAllByParentIdOrderBySequenceAsc(categoryId);
        return UserWebCategory.entityListToDtoList(result);
    }
}
