package com.capstone.shop.dto;

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
                .build();
    }
}