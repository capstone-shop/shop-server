package com.capstone.shop.entity;

import jakarta.persistence.*;
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

    // 부모 카테고리 변경 메서드
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
}
