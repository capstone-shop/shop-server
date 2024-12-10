package com.capstone.shop.core.domain.dto;

import com.capstone.shop.core.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserMeResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private int dealingCount;
    private int reputation;
    private String authProvider;
    private String profileImages;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserMeResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.dealingCount = user.getDealingCount();
        this.reputation = user.getReputation();
        this.profileImages = user.getProfileImages();
    }
}
