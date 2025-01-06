package com.capstone.shop.admin.v1.controller.dto;

import com.capstone.shop.core.domain.entity.Category;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryResponseDtos {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponseDto {
        private Long id;
        private String title;
        private String parentTitle;
        private boolean isLeaf;
        private Long sequence;
        private String registerName;

        @Builder
        public CategoryResponseDto(Category category) {
            this.id = category.getId();
            this.title = category.getTitle();
            this.parentTitle = category.getParent() != null ? category.getParent().getTitle() : null;
            this.isLeaf = category.isLeaf();
            this.sequence = category.getSequence();
            this.registerName = category.getRegister() != null ? category.getRegister().getName() : null;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryTreeResponseDto {
        private CategoryResponseDto category;
        private List<CategoryResponseDto> children;

        @Builder
        public CategoryTreeResponseDto(Category category, List<Category> children) {
            this.category = new CategoryResponseDto(category);
            this.children = children.stream()
                    .map(CategoryResponseDto::new)
                    .collect(Collectors.toList());
        }
    }
}