package com.capstone.shop.user.v1.controller;

import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseListAndPaginationResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseRegisterRequest;
import com.capstone.shop.user.v1.service.UserWebMerchandiseService;
import com.capstone.shop.user.v1.util.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchandise")
public class UserWebMerchandiseController {
    private final UserWebMerchandiseService userWebMerchandiseService;

    @GetMapping
    public MerchandiseListAndPaginationResponse getMerchandise(
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

        return userWebMerchandiseService.getMerchandise(sort, search, pageable, filterObj);
    }

    @PostMapping
    public ResponseEntity<String> createMerchandise(@RequestBody MerchandiseRegisterRequest request) {
        boolean result = userWebMerchandiseService.createMerchandise(request.toEntity());
        if (result) {
            return ResponseEntity.ok("success to create merchandise");
        }
        return ResponseEntity.badRequest().body("merchandise could not be created");
    }
}