package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByUserEmail(String userEmail);

    UserRefreshToken findByUserId(Long id);
    UserRefreshToken findByUserIdAndRefreshToken(Long userId, String refreshToken);
}
