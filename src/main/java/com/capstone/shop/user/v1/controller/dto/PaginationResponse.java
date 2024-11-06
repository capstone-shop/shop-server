package com.capstone.shop.user.v1.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
public class PaginationResponse {
    private int page;
    private int totalPages;
    private int requestSize;
    private int responseSize;
    private String sort;
    private String search;
    private boolean last;
    private boolean first;

    public PaginationResponse(int page, int size, String sort, String search, Page<?> entityPage) {
        this.page = page;
        this.totalPages = entityPage.getTotalPages();
        this.requestSize = size;
        this.responseSize = entityPage.getNumberOfElements();
        this.sort = sort;
        this.search = search;
        this.first = entityPage.isFirst();
        this.last = entityPage.isLast();
    }
}