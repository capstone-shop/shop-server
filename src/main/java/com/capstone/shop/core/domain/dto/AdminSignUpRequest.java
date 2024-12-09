package com.capstone.shop.core.domain.dto;

import com.capstone.shop.core.domain.enums.AuthProvider;
import com.capstone.shop.core.domain.enums.Role;
import com.capstone.shop.core.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminSignUpRequest {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone_number;
    private String profileImages;
    private AuthProvider authProvider;
    private Role role;

    //    public User toEntity() {
//        return User.builder()
//                .name(this.name)
//                .email(this.email)
//                .password(this.password)
//                .build();
//    }
//
    public static AdminSignUpRequest fromEntity(User user) {
        return AdminSignUpRequest.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .address(user.getAddress())
                .phone_number(user.getPhoneNumber())
                .profileImages(user.getProfileImages())
                .authProvider(user.getAuthProvider())
                .role(user.getRole())
                .build();
    }
}