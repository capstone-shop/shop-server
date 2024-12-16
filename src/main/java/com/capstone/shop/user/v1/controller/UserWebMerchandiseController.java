package com.capstone.shop.user.v1.controller;

import com.capstone.shop.core.security.CurrentUser;
import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebPostMerchandiseResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebWish;
import com.capstone.shop.user.v1.service.UserWebMerchandiseService;
import com.capstone.shop.user.v1.search.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Operation(summary = "상품 등록 api")
    @ApiResponse(content = @Content(examples = {
            @ExampleObject(name = "실패", value = "{\"success\":false,\"id\":0}"),
            @ExampleObject(name = "성공", value = "{\"success\":true,\"id\":101}")
    }))
    public UserWebPostMerchandiseResponse createMerchandise(@RequestBody UserWebMerchandiseRegister request,
            @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.createMerchandise(request, userPrincipal.getId());
    }

    @DeleteMapping("/{merchandiseId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "상품 삭제 api")
    @ApiResponse(content = @Content(examples = {
            @ExampleObject(name = "실패1", value = "{\"success\":false,\"message\":\"해당하는 상품이 없습니다.\"}"),
            @ExampleObject(name = "실패2", value = "{\"success\":false,\"message\":\"현재 사용자 계정에서 등록된 상품이 아닙니다.\"}"),
            @ExampleObject(name = "성공", value = "{\"success\":true,\"message\":\"삭제 완료\"}")
    }))
    public com.capstone.shop.core.domain.dto.ApiResponse deleteMerchandise(@PathVariable String merchandiseId,
            @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.deleteMerchandise(userPrincipal.getId(), Long.valueOf(merchandiseId));
    }

    @PutMapping("/{merchandiseId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "상품 수정 api")
    @ApiResponse(content = @Content(examples = {
            @ExampleObject(name = "실패1", value = "{\"success\":false,\"message\":\"해당하는 상품이 없습니다.\"}"),
            @ExampleObject(name = "실패2", value = "{\"success\":false,\"message\":\"현재 사용자 계정에서 등록된 상품이 아닙니다.\"}"),
            @ExampleObject(name = "실패3", value = "{\"success\":false,\"message\":\"해당하는 카테고리가 없습니다.\"}"),
            @ExampleObject(name = "실패4", value = "{\"success\":false,\"message\":\"사용자 계정이 조회되지 않습니다.\"}"),
            @ExampleObject(name = "성공", value = "{\"success\":true,\"message\":\"수정 완료\"}")
    }))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(name = "예시1", value = """
                    {
                      "images": [
                        "https://shop-s3bucket.s3.ap-northeast-2.amazonaws.com/1734323629995_0CeCMeTNN0PhcgO8xjPwmLo0bkFis32K6Z11_tfwPdt0ykEXqK1cK39-0fCodUVe4UMNkxtKwHKTz4nPaC5WN7kHI5evxE-LYvebl51owscTePWnrlJ5gdDRhnsvJFOUnQuH_YZx3FWk3HdI5gtG0w.png"
                      ],
                      "name": "보노보노",
                      "categoryId": 78,
                      "state": "NEW",
                      "description": "보노보노",
                      "price": 10000,
                      "direct": true,
                      "nego": true,
                      "delivery": true,
                      "location": "후곡로 50"
                    }""")
    }))
    public com.capstone.shop.core.domain.dto.ApiResponse putMerchandise(@PathVariable String merchandiseId,
            @RequestBody UserWebMerchandiseRegister request,
            @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.putMerchandise(userPrincipal.getId(), Long.valueOf(merchandiseId), request);
    }

    @PatchMapping("/{id}/wish")   //요청 보내면 위시카운트 + 1 , 위시리스트 등록, 만약 위시리스트에 이미 있는 상품이면 위시카운트 -1
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "찜 토글 api")
    public UserWebWish addOrSubWish(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.toggleWish(id, userPrincipal.getId());
    }

    @GetMapping("/{id}/wish")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "찜 여부 조회 api")
    public UserWebWish getWish(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal) {
        return userWebMerchandiseService.wishCount(id, userPrincipal.getId());
    }
}
