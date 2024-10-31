package com.capstone.shop.user.v1.controller;

import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandisePaginationResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseRegisterRequest;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseResponse;
import com.capstone.shop.user.v1.dto.PaginationResponse;
import com.capstone.shop.user.v1.service.MerchandiseService;
import com.capstone.shop.entity.Merchandise;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
public class MerchandiseController {
    private final MerchandiseService merchandiseService;

    @GetMapping
    public MerchandisePaginationResponse getMerchandise(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "like,desc", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        Page<Merchandise> merchandisePage = merchandiseService.getMerchandise(search, pageable);

        List<MerchandiseResponse> merchandiseList = MerchandiseResponse.getMerchandiseResponses(merchandisePage);
        PaginationResponse pagination = new PaginationResponse(page, size, sort, search, merchandisePage);

        return new MerchandisePaginationResponse(merchandiseList, pagination);
    }

    @PostMapping
    public ResponseEntity<String> createMerchandise(@RequestBody MerchandiseRegisterRequest request) {
        boolean result = merchandiseService.createMerchandise(request.toEntity());
        if (result) {
            return ResponseEntity.ok("success to create merchandise");
        }
        return ResponseEntity.badRequest().body("merchandise could not be created");
    }
}
