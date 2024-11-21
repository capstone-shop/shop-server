package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.PaginatedResponse;
import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.entity.User;
import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.dto.SignUpRequest;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ApiResponse createUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + signUpRequest.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .authProvider(signUpRequest.getAuthProvider())
                .address(signUpRequest.getAddress())
                .phoneNumber(signUpRequest.getPhone_number()) // JSON 필드명 통일
                .profileImages(signUpRequest.getProfileImages())
                .role(signUpRequest.getRole())
                .build();

        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully");
    }


    @Override
    public ApiResponse updateUser(SignUpRequest signUpRequest) {
        User user = userRepository.findByEmail(signUpRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + signUpRequest.getEmail()));

        // 의도치 않게 비밀번호가 null 인 가능성 배제
        if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }

        user.setName(signUpRequest.getName());
        user.setAuthProvider(signUpRequest.getAuthProvider());
        user.setAddress(signUpRequest.getAddress());
        user.setPhoneNumber(signUpRequest.getPhone_number()); // JSON 필드명 통일
        user.setProfileImages(signUpRequest.getProfileImages());
        user.setRole(signUpRequest.getRole());

        userRepository.save(user);

        return new ApiResponse(true, "유저 업데이트 성공");
    }
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return new UserResponseDto(user);
    }



    public PaginatedResponse<UserResponseDto> getAllUsers(int page, int size, String sort, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        // 검색어가 있으면 검색 조건을 추가하여 사용자 검색
        Page<User> userPage;
        if (search != null && !search.isEmpty()) {
            List<User> users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
            // 검색된 결과로 페이지화된 객체를 만들어서 반환
            userPage = new PageImpl<>(users, pageable, users.size());
        } else {
            // 검색어가 없으면 모든 사용자 조회
            userPage = userRepository.findAll(pageable);
        }

        // User -> UserResponseDto 변환
        List<UserResponseDto> userResponseDtos = userPage.getContent()
                .stream()
                .map(UserResponseDto::new)
                .toList();

        // PaginatedResponse 객체 생성 및 반환
        return new PaginatedResponse<>(
                page,
                size,
                sort,
                search, // 검색어 전달
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userResponseDtos
        );
    }


    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없음" + id));
        userRepository.delete(user);
    }

}
