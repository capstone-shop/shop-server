package com.capstone.shop.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateApiResponse {
    private boolean success;
    private String message;

    @Schema(description = "실패시 0, 성공시 기본키")
    private long id;

    public CreateApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.id = 0;
    }
}
