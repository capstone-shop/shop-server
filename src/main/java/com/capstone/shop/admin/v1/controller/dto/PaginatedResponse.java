package com.capstone.shop.admin.v1.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter@Setter
public class PaginatedResponse<T> {
    private int page;
    private int size;
    private String sort;
    private String search;
    private long totalElements;
    private int totalPages;
    private List<T> content;

    public PaginatedResponse(int page, int size, String sort, String search, long totalElements, int totalPages, List<T> content) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.search = search;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    // Getters and Setters
}