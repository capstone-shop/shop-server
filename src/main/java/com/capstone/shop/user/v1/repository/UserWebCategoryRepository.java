package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWebCategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentOrderBySequenceAsc(Category parent);

    List<Category> findAllByParentIdOrderBySequenceAsc(long categoryId);
}
