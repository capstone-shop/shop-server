package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByUserEmail(String userEmail);

    UserRefreshToken findByUserId(Long id);
    UserRefreshToken findByUserIdAndRefreshToken(Long userId, String refreshToken);
}
