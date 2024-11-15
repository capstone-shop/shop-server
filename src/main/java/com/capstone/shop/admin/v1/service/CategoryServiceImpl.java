package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.repository.CategoryRepository;
import com.capstone.shop.entity.Category;
import com.capstone.shop.entity.User;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse createCategory(CategoryRequestDto categoryRequestDto) {
        Category parentCategory = null;


        String parentTitle = categoryRequestDto.getParent();
        String title = categoryRequestDto.getTitle();

        // 부모 카테고리 찾기
        if (parentTitle != null && !parentTitle.isEmpty()) {
            parentCategory = categoryRepository.findByTitle(parentTitle)
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리가 존재하지 않습니다: " + parentTitle));
        }
        // 제목 중복 검사
        if (categoryRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 제목입니다: " + title);
        }
        User user = userRepository.findByName(categoryRequestDto.getRegister()).orElseThrow(()->new IllegalArgumentException("작성자를 찾을 수 없음"));
        Category category = Category.builder()
                .title(title)
                .parent(parentCategory)
                .isLeaf(categoryRequestDto.isLeaf())
                .sequence(categoryRequestDto.getSequence())
                .register(user)
                .build();

        categoryRepository.save(category);
        return new ApiResponse(true, "카테고리 생성 성공");
    }

    @Override
    public ApiResponse updateCategory(CategoryRequestDto categoryRequestDto) {

    }


}
