package com.capstone.shop.admin.v1.controller.dto;

import com.capstone.shop.core.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    private Long id;

    private String title;

    private boolean isLeaf;

    private Long parentId;

    private Long sequence;

    //private String register;

    public Category toEntity(Category parentCategory) {
        return Category.builder()
                .id(this.id)
                .title(this.title)
                .isLeaf(this.isLeaf)
                .parent(parentCategory)  // 부모 카테고리를 설정
                .build();
    }

    public static CategoryRequestDto fromEntity(Category category) {
        return new CategoryRequestDto(
                category.getId(),
                category.getTitle(),
                category.isLeaf(),  //이거 getIsLeaf()랑 같음
                category.getParent().getId() != null ? category.getParent().getId() : null,
                category.getSequence()
        );
    }

}
