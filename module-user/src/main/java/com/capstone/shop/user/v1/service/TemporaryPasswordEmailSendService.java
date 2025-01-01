package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemporaryPasswordEmailSendService {
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String generateTemporaryPassword(){
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendTemporaryPasswordAndChangePassword(Long id) throws MessagingException{
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String temporaryPassword = generateTemporaryPassword();
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("임시 비밀번호가 발급되었습니다.");
        helper.setText("임시 비밀번호: " + temporaryPassword + "\n\n추후 비밀번호를 변경해 주세요.");

        emailSender.send(message);

        // 비밀번호 변경 및 저장
        user.setPassword(passwordEncoder.encode(temporaryPassword)); // 암호화 후 저장
        userRepository.save(user);
    }
}
