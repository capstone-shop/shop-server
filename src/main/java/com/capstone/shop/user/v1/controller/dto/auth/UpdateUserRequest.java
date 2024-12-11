package com.capstone.shop.user.v1.controller.dto.auth;

import com.capstone.shop.core.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserRequest {
    private String name;
    private String email;
    private String address;
    private String phone_number;
    private String profileImages;
    public static UpdateUserRequest fromEntity(User user) {
        return UpdateUserRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone_number(user.getPhoneNumber())
                .profileImages(user.getProfileImages())
                .build();
    }
}

