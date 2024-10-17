package com.capstone.shop.user.v1.service;

import com.capstone.shop.dto.SignUpRequest;
import com.capstone.shop.entity.AuthProvider;
import com.capstone.shop.entity.User;
import com.capstone.shop.exception.BadRequestException;
import com.capstone.shop.security.TokenProvider;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public String signIn(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }

    @Override
    public SignUpRequest signUpUser(String name, String email, String password) {
        if(userRepository.existsByEmail(email)){
            throw new BadRequestException("Email address already Exist.");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                //.authProvider(authP)
                .build();
        User savedUser = userRepository.save(user);

        return SignUpRequest.fromEntity(savedUser);

    }
}