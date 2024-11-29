package com.capstone.shop.user.v1.controller.dto.auth;
import lombok.Getter;

@Getter
public class OAuth2AdditionalInfoRequest {
    private String address;
    private String phoneNumber;

}
