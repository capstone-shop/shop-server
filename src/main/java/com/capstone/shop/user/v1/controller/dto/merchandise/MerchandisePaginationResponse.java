package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.user.v1.dto.PaginationResponse;
import java.util.List;

import com.capstone.shop.user.v1.dto.PaginationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MerchandisePaginationResponse {
    private List<MerchandiseResponse> merchandise;
    private PaginationResponse pagination;
}