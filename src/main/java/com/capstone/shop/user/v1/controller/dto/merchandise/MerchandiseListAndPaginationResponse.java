package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MerchandiseListAndPaginationResponse {
    private List<MerchandiseResponse> merchandise;
    private PaginationResponse pagination;
}