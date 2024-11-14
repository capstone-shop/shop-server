package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryRequestDto createCategory(
            String title, String parent);
}
