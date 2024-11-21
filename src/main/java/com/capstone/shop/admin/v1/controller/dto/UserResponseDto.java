package com.capstone.shop.admin.v1.controller.dto;

import com.capstone.shop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String authProvider;
    private String address;
    private String phoneNumber;
    private String profileImages;
    private String role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.authProvider = user.getAuthProvider().name();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImages = user.getProfileImages();
        this.role = user.getRole().name();
    }
}