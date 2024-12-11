package com.capstone.shop.user.v1.controller;

import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.security.CurrentUser;
import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebWish;
import com.capstone.shop.user.v1.service.UserWebMerchandiseService;
import com.capstone.shop.user.v1.search.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchandise")
@Tag(name = "UserWebMerchandiseController", description = "유저 상품 관련 컨트롤러")
public class UserWebMerchandiseController {
    private final UserWebMerchandiseService userWebMerchandiseService;

    @GetMapping
    public UserWebMerchandisePagination getMerchandise(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "wish,desc", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "filter", defaultValue = "", required = false) String filter) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);

        Filter filterObj = new Filter(filter);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        return userWebMerchandiseService.getMerchandise(search, pageable, filterObj);
    }

    @GetMapping("/{merchandiseId}")
    public UserWebMerchandiseDetail getMerchandise(@PathVariable String merchandiseId) {

        return userWebMerchandiseService.getMerchandise(Long.valueOf(merchandiseId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createMerchandise(@RequestBody UserWebMerchandiseRegister request,
            @CurrentUser UserPrincipal userPrincipal) {
        boolean result = userWebMerchandiseService.createMerchandise(request, userPrincipal.getId());
        if (result) {
            return ResponseEntity.ok("success to create merchandise");
        }
        return ResponseEntity.badRequest().body("merchandise could not be created");
    }

    @PatchMapping("/{id}/wish")   //요청 보내면 위시카운트 + 1 , 위시리스트 등록, 만약 위시리스트에 이미 있는 상품이면 위시카운트 -1
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "찜 토글 api")
    public ApiResponse addOrSubWish(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.toggleWish(id, userPrincipal.getId());
    }

    @GetMapping("/{id}/wish")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "찜 여부 조회 api")
    public UserWebWish getWish(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.wishCount(id, userPrincipal.getId());
    }
}
