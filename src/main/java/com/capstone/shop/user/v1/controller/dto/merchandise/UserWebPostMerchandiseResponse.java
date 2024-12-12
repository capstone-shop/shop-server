package com.capstone.shop.user.v1.controller.dto.merchandise;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserWebPostMerchandiseResponse {
    private boolean success;

    @Schema(description = "실패시 0, 성공시 기본키")
    private long id;
}
