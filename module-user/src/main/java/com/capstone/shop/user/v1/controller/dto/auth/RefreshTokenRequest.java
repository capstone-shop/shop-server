package com.capstone.shop.user.v1.controller.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private String userId;


    private String refreshToken;
}
