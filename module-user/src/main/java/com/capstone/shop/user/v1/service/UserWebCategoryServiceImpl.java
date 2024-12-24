package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.Category;
import com.capstone.shop.core.domain.repository.CategoryRepository;
import com.capstone.shop.user.v1.controller.dto.category.UserWebCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWebCategoryServiceImpl implements UserWebCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<UserWebCategory> getCategory() {
        List<Category> result = categoryRepository.findAllByParentOrderBySequenceAsc(null);
        return UserWebCategory.entityListToDtoList(result);
    }

    @Override
    public List<UserWebCategory> getCategory(long categoryId) {
        List<Category> result = categoryRepository.findAllByParentIdOrderBySequenceAsc(categoryId);
        return UserWebCategory.entityListToDtoList(result);
    }
}
