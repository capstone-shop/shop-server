package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.repository.CategoryRepository;
import com.capstone.shop.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryRequestDto createCategory(String title, String parentTitle) {
        Category parentCategory = null;
        boolean isLeaf = false;

        // parentTitle이 있으면 부모 카테고리를 찾음
        if (parentTitle != null && !parentTitle.isEmpty()) {
            parentCategory = categoryRepository.findByTitle(parentTitle)
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리가 존재하지 않습니다: " + parentTitle));

            // 부모의 깊이에 따라 isLeaf 결정
            isLeaf = (parentCategory.getParent() != null);
        }

        Category category = Category.builder()
                .title(title)
                .parent(parentCategory)
                .isLeaf(isLeaf)
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryRequestDto.fromEntity(savedCategory);
    }
}
