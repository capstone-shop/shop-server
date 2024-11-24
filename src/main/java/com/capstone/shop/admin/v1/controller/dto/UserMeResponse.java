package com.capstone.shop.admin.v1.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserMeResponse {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private int dealingCount;
    private int reputation;
    private String authProvider;
    private String profileImages;
}
