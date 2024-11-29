package com.capstone.shop.core.domain.entity;

import jakarta.persistence.*;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isLeaf;

    @Column(nullable = false)
    private Long sequence;

    @ManyToOne
    @JoinColumn(name = "register_id", nullable = false)
    private User register;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Merchandise> merchandiseList = new ArrayList<>();

    // 기존 메서드들
    public void updateIsLeaf(boolean isLeaf) {
        if (this.isLeaf != isLeaf) {
            this.isLeaf = isLeaf;
        }
    }

    public void updateSequence(Long sequence) {
        if (!this.sequence.equals(sequence)) {
            this.sequence = sequence;
        }
    }

    public void changeParentCategory(Category newParent) {
        if (newParent == null) {
            throw new IllegalArgumentException("부모 카테고리는 null일 수 없습니다.");
        }
        this.parent = newParent;
    }

    public void changeRegister(User newUser){
        if(newUser == null) {
            throw new IllegalArgumentException("수정유저는 null일 수 없어요");
        }
        this.register = newUser;
    }

    // 대 카테고리부터 이 메소드가 호출된 카테고리 까지 리스트를 반환.
    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(this);
        Category parent = this.parent;
        while (parent != null) {
            categoryList.add(parent);
            parent = parent.getParent();
        }
        Collections.reverse(categoryList);
        return categoryList;
    }
}
