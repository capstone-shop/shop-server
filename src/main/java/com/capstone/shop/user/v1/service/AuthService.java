package com.capstone.shop.user.v1.service;

import com.capstone.shop.dto.SignUpRequest;

public interface AuthService {
    String signIn(String email, String password);
    SignUpRequest signUpUser(String name, String email, String password);
}
