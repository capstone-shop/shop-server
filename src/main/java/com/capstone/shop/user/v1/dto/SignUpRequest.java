package com.capstone.shop.user.v1.dto;

import com.capstone.shop.enums.AuthProvider;
import com.capstone.shop.enums.Role;
import com.capstone.shop.entity.User;
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
    private String phoneNumber;
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
    public static SignUpRequest fromEntity(User user) {
        return SignUpRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .profileImages(user.getProfileImages())
                .authProvider(user.getAuthProvider())
                .role(user.getRole())
                .build();
    }
}