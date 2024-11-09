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
    private Integer sequence;

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
}
