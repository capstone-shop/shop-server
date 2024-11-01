package com.capstone.shop.user.v1.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListAndPaginationResponse<Dto> {
    private List<Dto> merchandise;
    private PaginationResponse pagination;
}