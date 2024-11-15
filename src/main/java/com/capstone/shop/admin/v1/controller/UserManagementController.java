package com.capstone.shop.admin.v1.controller;

import com.capstone.shop.admin.v1.controller.dto.UserResponseDto;
import com.capstone.shop.admin.v1.service.UserManagementService;
import com.capstone.shop.entity.User;
import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.dto.SignUpRequest;
import com.capstone.shop.user.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(@RequestBody SignUpRequest signUpRequest) {
        ApiResponse response = userManagementService.createUser(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody SignUpRequest signUpRequest) {
        ApiResponse response = userManagementService.updateUser(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto user = userManagementService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<PaginationResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "") String search) {

        try {
            Page<User> userPage;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

            if (search != null && !search.trim().isEmpty()) {
                // 검색어가 있는 경우: username이나 email에 검색어가 포함된 사용자 검색
                userPage = userRepository.findByUsernameContainingOrEmailContaining(
                        search.trim(), search.trim(), pageable);
            } else {
                // 검색어가 없는 경우: 전체 사용자 조회
                userPage = userRepository.findAll(pageable);
            }

            // 페이징 결과를 PaginationResponse 객체로 변환
            PaginationResponse response = new PaginationResponse(
                    page,
                    size,
                    sort,
                    search,
                    userPage
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
