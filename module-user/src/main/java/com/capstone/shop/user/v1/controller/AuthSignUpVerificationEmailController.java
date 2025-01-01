package com.capstone.shop.user.v1.controller;

import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.user.v1.service.VerificationEmailSendService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthSignUpVerificationEmailController {

    private final VerificationEmailSendService verificationEMailSendService;

    @PostMapping("/email/verify/send")
    public ResponseEntity<ApiResponse> sendVerificationEmail(@RequestParam @Email String emailAddress) {
        try {
            verificationEMailSendService.sendVerificationCode(emailAddress);
            return ResponseEntity.ok(new ApiResponse(true, "코드 전송 성공."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "코드 전송 실패."));
        }
    }

    @PostMapping("/email/verify/confirm")
    public ResponseEntity<ApiResponse> verifyEmailCode(
            @RequestParam @Email String emailAddress,
            @RequestParam String code) {
        boolean isVerified = verificationEMailSendService.verifyCode(emailAddress, code);

        if (isVerified) {
            return ResponseEntity.ok(new ApiResponse(true, "메일 인증 성공."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "메일 인증 실패."));
        }
    }
}