package com.capstone.shop.user.v1.controller.dto.auth;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor

public class EmailVerificationInfoDto {
    private String code;
    private LocalDateTime expiryTime;
}
