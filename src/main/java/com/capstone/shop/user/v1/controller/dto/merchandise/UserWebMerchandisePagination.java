package com.capstone.shop.user.v1.controller.dto.merchandise;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserWebMerchandisePagination {
    private List<UserWebMerchandise> merchandise;
    private Integer totalPage;
}