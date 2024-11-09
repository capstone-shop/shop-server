package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.category.UserWebCategoryResponse;
import com.capstone.shop.user.v1.repository.UserWebCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWebCategoryServiceImpl implements UserWebCategoryService {

    private final UserWebCategoryRepository userWebCategoryRepository;

    @Override
    public List<UserWebCategoryResponse> getUserWebCategory() {
        var result = userWebCategoryRepository.findAllByParentOrderBySequenceAsc(null);
        return UserWebCategoryResponse.entityListToDtoList(result);
    }
}
