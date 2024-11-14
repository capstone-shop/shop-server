package com.capstone.shop.admin.v1.controller.dto;

import com.capstone.shop.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    private String title;

    //private boolean isLeaf;

    private String parent;

    public Category toEntity(Category parentCategory) {
        return Category.builder()
                .title(this.title)
                //.isLeaf(this.isLeaf)
                .parent(parentCategory)  // 부모 카테고리를 설정
                .build();
    }

    public static CategoryRequestDto fromEntity(Category category) {
        return new CategoryRequestDto(
                category.getTitle(),
                //category.isLeaf(),
                category.getParent() != null ? category.getParent().getTitle() : null
        );
    }

}
