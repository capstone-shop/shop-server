package com.capstone.shop.user.v1.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;
@Data
@AllArgsConstructor

public class EmailVerificationInfoDto {
    private String code;
    private LocalDateTime expiryTime;
}
