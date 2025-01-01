package com.capstone.shop.user.v1.controller;

import com.capstone.shop.core.security.CurrentUser;
import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.myinfo.UserWebPageRequest;
import com.capstone.shop.user.v1.service.UserWebMyInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/my-info")
@Tag(name = "MyInfoController", description = "유저 정보 관련 컨트롤러(로그인 필요)")
public class MyInfoController {

    private final UserWebMyInfoService myInfoService;

    @GetMapping("/wishlist")
    @Operation(summary = "찜한 상품 조회 api(최근에 찜한 물건부터)")
    @PreAuthorize("hasRole('USER')")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호 (기본값 0)", allowEmptyValue = true),
            @Parameter(name = "size", description = "한번에 보여줄 갯수 (기본값 20)", allowEmptyValue = true)
    })
    public UserWebMerchandisePagination getWishlist(
            @CurrentUser UserPrincipal userPrincipal,
            @Schema(hidden = true) UserWebPageRequest paginationRequest) {

        return myInfoService.getWishlist(userPrincipal.getId(), paginationRequest.toPageable());
    }

    @GetMapping("/registered-merchandise")
    @Operation(summary = "등록한 상품 목록 조회 api(최근에 등록한 물건부터)")
    @PreAuthorize("hasRole('USER')")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호 (기본값 0)", allowEmptyValue = true),
            @Parameter(name = "size", description = "한번에 보여줄 갯수 (기본값 20)", allowEmptyValue = true)
    })
    public UserWebMerchandisePagination getRegisteredMerchandise(
            @CurrentUser UserPrincipal userPrincipal,
            @Schema(hidden = true) UserWebPageRequest paginationRequest) {

        return myInfoService.getRegisteredMerchandise(userPrincipal.getId(), paginationRequest.toPageable());
    }
}