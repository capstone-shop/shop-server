package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.CategoryRequestDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryResponseDto;
import com.capstone.shop.admin.v1.controller.dto.CategoryResponseDtos.CategoryTreeResponseDto;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.entity.Category;
import com.capstone.shop.entity.User;
import com.capstone.shop.admin.v1.repository.CategoryRepository;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
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

        User user = userRepository.findByName(categoryRequestDto.getRegister())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없음"));

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
    @Transactional
    public ApiResponse updateCategory(CategoryRequestDto categoryRequestDto, Long id) {
        // 기존 카테고리 찾기
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 카테고리를 찾을 수 없습니다: " + categoryRequestDto.getTitle()));

        // 부모 카테고리 처리
        Category parentCategory = null;
        if (categoryRequestDto.getParent() != null && !categoryRequestDto.getParent().isEmpty()) {
            parentCategory = categoryRepository.findByTitle(categoryRequestDto.getParent())
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리가 존재하지 않습니다: " + categoryRequestDto.getParent()));
        }

        // 작성자 확인
        User user = userRepository.findByName(categoryRequestDto.getRegister())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없음"));

        // 카테고리 정보 업데이트
        existingCategory.changeParentCategory(parentCategory);
        existingCategory.updateIsLeaf(categoryRequestDto.isLeaf());
        existingCategory.updateSequence(categoryRequestDto.getSequence());
        existingCategory.changeRegister(user);

        // 카테고리 업데이트 후 저장
        categoryRepository.save(existingCategory);
        return new ApiResponse(true, "카테고리 수정 성공");
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
    public CategoryTreeResponseDto getCategoriesByParent(String parentTitle) {
        // 부모 카테고리 찾기
        Category parentCategory = categoryRepository.findByTitle(parentTitle)
                .orElseThrow(() -> new IllegalArgumentException("부모 카테고리를 찾을 수 없습니다: " + parentTitle));

        // 해당 부모의 자식 카테고리 조회
        List<Category> childCategories = categoryRepository.findByParent(parentCategory);

        return new CategoryTreeResponseDto(parentCategory, childCategories);
    }
}
