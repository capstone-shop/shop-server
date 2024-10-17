package com.capstone.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponse {
    private String tokenType = "Bearer";
    private String accessToken;
    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}