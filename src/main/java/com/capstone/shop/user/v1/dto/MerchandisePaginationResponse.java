package com.capstone.shop.user.v1.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class MerchandisePaginationResponse {
    private List<MerchandiseResponse> merchandise;
    private PaginationResponse pagination;
}