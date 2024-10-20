package com.capstone.shop.user.v1.dto;

import com.capstone.shop.entity.Merchandise;
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

    public PaginationResponse(int page, int size, String sort, String search, Page<Merchandise> merchandisePage) {
        this.page = page;
        this.totalPages = merchandisePage.getTotalPages();
        this.requestSize = size;
        this.responseSize = merchandisePage.getNumberOfElements();
        this.sort = sort;
        this.search = search;
        this.first = merchandisePage.isFirst();
        this.last = merchandisePage.isLast();
    }
}