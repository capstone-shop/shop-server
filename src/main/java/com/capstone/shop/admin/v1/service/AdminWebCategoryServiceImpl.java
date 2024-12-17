package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryResponseDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryTreeResponseDto;
import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.dto.CreateApiResponse;
import com.capstone.shop.core.domain.entity.Category;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.repository.CategoryRepository;
import com.capstone.shop.core.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminWebCategoryServiceImpl implements AdminWebCategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CreateApiResponse createCategory(Long userId, CategoryRequestDto categoryRequestDto) {
        Category parentCategory = null;
        Long parentId = categoryRequestDto.getParentId();

        // 부모 카테고리 찾기
        if (parentId != null && parentId > 0) {
            parentCategory = categoryRepository.findById(parentId).orElse(null);
            if (parentCategory == null) {
                return new CreateApiResponse(false, "카테고리 생성 : 전달된 부모 카테고리가 존재하지 않습니다.");
            }
        }

        // 생성한 유저 찾기
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new CreateApiResponse(false, "카테고리 생성 : 현재 로그인된 관리 정보를 찾을수 없습니다.");
        }

        Category category = categoryRepository.save(categoryRequestDto.toEntity(parentCategory, user));
        return new CreateApiResponse(true, "카테고리 생성 : 성공", category.getId());
    }

    @Override
    @Transactional
    public ApiResponse updateCategory(Long userId, CategoryRequestDto categoryRequestDto, Long id) {
        // 기존 카테고리 찾기
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            return new ApiResponse(false, "카테고리 수정 : 수정할 카테고리가 존재하지 않습니다.");
        }

        // 부모 카테고리 찾기
        Category parentCategory = null;
        Long parentId = categoryRequestDto.getParentId();
        if (parentId != null && parentId > 0) {
            parentCategory = categoryRepository.findById(parentId).orElse(null);
            if (parentCategory == null) {
                return new ApiResponse(false, "카테고리 수정 : 전달된 부모 카테고리가 존재하지 않습니다.");
            }
        }

        // 작성자 확인
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "카테고리 수정 : 현재 로그인된 관리자 정보를 찾을수 없습니다.");
        }

        // 카테고리 업데이트 후 저장
        categoryRepository.save(categoryRequestDto.toEntity(parentCategory, user, id));
        return new ApiResponse(true, "카테고리 수정 : 성공");
    }

    @Override
    @Transactional
    public ApiResponse deleteCategory(Long id) { //id가 인자가 아닌 이유는 카테고리 엔티티가 자기참조 구조라서..
        // 삭제할 카테고리 찾기
        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 카테고리를 찾을 수 없습니다: " + id));

        // 자식 카테고리 확인
        List<Category> childCategories = categoryRepository.findByParent(categoryToDelete);
        if (!childCategories.isEmpty()) {
            throw new IllegalArgumentException("하위 카테고리가 있어 삭제할 수 없습니다: " + id);
        }

        categoryRepository.delete(categoryToDelete);
        return new ApiResponse(true, "카테고리 삭제 성공");
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByTitle(String categoryTitle) {
        Category category = categoryRepository.findByTitle(categoryTitle)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다: " + categoryTitle));

        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryTreeResponseDto getCategoriesByParent(Long parentId) {
        // 부모 카테고리 찾기
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 카테고리를 찾을 수 없습니다: " + parentId));

        // 해당 부모의 자식 카테고리 조회
        List<Category> childCategories = categoryRepository.findByParent(parentCategory);

        return new CategoryTreeResponseDto(parentCategory, childCategories);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllMainCategories() {
        List<Category> categories = categoryRepository.findByParentIsNull();
        return categories.stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }
}
