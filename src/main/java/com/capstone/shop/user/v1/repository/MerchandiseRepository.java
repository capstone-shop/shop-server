package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.Merchandise;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchandiseRepository extends JpaRepository<Merchandise, Long> {
    Page<Merchandise> findByNameContaining(String search, Pageable pageable);

    List<Merchandise> findTop3ByOrderByCreatedAtDesc();

    List<Merchandise> findTop3ByOrderByModifiedAtDesc();
}
