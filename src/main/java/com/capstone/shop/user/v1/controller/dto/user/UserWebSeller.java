package com.capstone.shop.user.v1.controller.dto.user;

import com.capstone.shop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserWebSeller {
    private Long id;
    private String name;
    private Integer reputation;

    public UserWebSeller(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.reputation = user.getReputation();
    }
}
