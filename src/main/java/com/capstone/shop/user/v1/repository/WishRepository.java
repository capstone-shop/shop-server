package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.entity.User;
import com.capstone.shop.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Optional<Wish> findByUserAndMerchandise(User user, Merchandise merchandise);
}
