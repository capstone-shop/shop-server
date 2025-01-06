package com.capstone.shop.user.v1.controller.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInRequest {
    private String email;
    private String password;
}
