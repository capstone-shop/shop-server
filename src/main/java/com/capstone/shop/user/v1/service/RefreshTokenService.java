package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.UserRefreshToken;
import com.capstone.shop.user.v1.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String userId, String refreshToken) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserId(userId);
        if (userRefreshToken != null) {
            userRefreshToken.setRefreshToken(refreshToken);
        } else {
            userRefreshToken = new UserRefreshToken(userId, refreshToken);
        }
        refreshTokenRepository.save(userRefreshToken);
    }

    public boolean verifyRefreshToken(String userId, String refreshToken) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        return userRefreshToken != null;  //true or false 반환
    }

    public void deleteRefreshToken(String userId) {
        UserRefreshToken userRefreshToken = refreshTokenRepository.findByUserId(userId);
        if (userRefreshToken != null) {
            refreshTokenRepository.delete(userRefreshToken);
        }
    }
}