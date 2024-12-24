package com.capstone.shop.admin.v1.controller.dto;

import com.capstone.shop.core.domain.entity.Category;
import com.capstone.shop.core.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    private String title;

    private boolean isLeaf;

    private Long parentId;

    private Long sequence;

    @JsonProperty("isLeaf")
    public boolean getIsLeaf() {
        return isLeaf;
    }

    public Category toEntity(Category parentCategory, User user) {
        return Category.builder()
                .title(this.title)
                .isLeaf(this.isLeaf)
                .parent(parentCategory)
                .sequence(this.sequence)
                .register(user)
                .build();
    }

    public Category toEntity(Category parentCategory, User user, Long id) {
        return Category.builder()
                .id(id)
                .title(this.title)
                .isLeaf(this.isLeaf)
                .parent(parentCategory)
                .sequence(this.sequence)
                .register(user)
                .build();
    }
}
