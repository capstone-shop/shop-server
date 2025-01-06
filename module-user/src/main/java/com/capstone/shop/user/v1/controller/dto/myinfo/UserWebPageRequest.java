package com.capstone.shop.user.v1.controller.dto.myinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Schema()
public class UserWebPageRequest {
    int page;
    int size;

    public UserWebPageRequest(Integer page, Integer size) {
        this.page = page == null ? 0 : page;
        this.size = size == null ? 20 : size;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
