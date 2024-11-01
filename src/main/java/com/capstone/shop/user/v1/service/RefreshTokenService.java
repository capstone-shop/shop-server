package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.User;
import com.capstone.shop.entity.UserRefreshToken;
import com.capstone.shop.user.v1.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(User user, String refreshToken) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserId(user.getId());
        if (userRefreshToken != null) {
            userRefreshToken.setRefreshToken(refreshToken);
        } else {
            userRefreshToken = new UserRefreshToken(user, refreshToken);
        }
        refreshTokenRepository.save(userRefreshToken);
    }

    public boolean verifyRefreshToken(Long userId, String refreshToken) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        return userRefreshToken != null;  //true or false 반환
    }

    public void deleteRefreshToken(Long userId) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserId(userId);
        if (userRefreshToken != null) {
            refreshTokenRepository.delete(userRefreshToken);
        }
    }
}