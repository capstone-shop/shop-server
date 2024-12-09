package com.capstone.shop.user.v1.controller.dto.auth;

import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.enums.AuthProvider;
import com.capstone.shop.core.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone_number;
    private String profileImages;
    public static SignUpRequest fromEntity(User user) {
        return SignUpRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone_number(user.getPhoneNumber())
                .profileImages(user.getProfileImages())
                .build();
    }

}