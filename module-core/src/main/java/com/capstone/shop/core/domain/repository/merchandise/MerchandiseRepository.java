package com.capstone.shop.core.domain.repository.merchandise;

import com.capstone.shop.core.domain.entity.Merchandise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchandiseRepository extends JpaRepository<Merchandise, Long>, JpaSpecificationExecutor<Merchandise> {
    @Override
    @NonNull
    Page<Merchandise> findAll(Specification<Merchandise> specification, @NonNull Pageable pageable);

    boolean existsById(Long id);
}
