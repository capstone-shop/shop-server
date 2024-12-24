package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.Merchandise;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.entity.Wish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Optional<Wish> findByUserAndMerchandise(User user, Merchandise merchandise);
}
