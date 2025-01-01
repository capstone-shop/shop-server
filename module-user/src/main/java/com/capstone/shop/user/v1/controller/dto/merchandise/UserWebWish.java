package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserWebWish {
    @Schema(defaultValue = "1", description = "물품 id")
    private Long merchandiseId;
    @Schema(defaultValue = "true", description = "찜 했는지 여부")
    private boolean isWished;

    @JsonProperty("isWished")
    public boolean getIsWished() {
        return isWished;
    }
}
