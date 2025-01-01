package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentOrderBySequenceAsc(Category parent);

    List<Category> findAllByParentIdOrderBySequenceAsc(long categoryId);

    Optional<Category> findByTitle(String title);

    boolean existsByTitle(String title);

    List<Category> findByParent(Category parent);

    List<Category> findByParentIsNull();
}
