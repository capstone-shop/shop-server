package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.auth.EmailVerificationInfoDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationEmailSendService {

    private final JavaMailSender emailSender;

    private final Map<String, EmailVerificationInfoDto> verificationCodes = new ConcurrentHashMap<>();
    //Redis를 사용하는게 보통인데, 사용할 수 없으니까 메모리를 사용함.
    //ConcurrnetHashMap은 Thead-safety한 해쉬맵이라고 함.


    private String generateRandomCode() {   //6자리 랜덤 코드 생성
        return String.format("%06d", new Random().nextInt(999999));
    }
    public void sendVerificationCode(String to) throws MessagingException { //to는 보낼 이메일 대상
        String code = generateRandomCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);  //현재로부터 5분뒤가 만료ㅕ시간

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("이메일 인증 코드");
        helper.setText("인증 코드: " + code + "\n\n이 코드는 5분간 유효합니다.");

        emailSender.send(message);

        verificationCodes.put(to, new EmailVerificationInfoDto(code, expiryTime));

        // 5분 후 자동 삭제
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                verificationCodes.remove(to);
            }
        }, 300000); // 5분
    }

    public boolean verifyCode(String email, String code) {
        EmailVerificationInfoDto info = verificationCodes.get(email);
        if (info == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(info.getExpiryTime())) {
            verificationCodes.remove(email);
            return false;
        }

        return info.getCode().equals(code);
    }

}