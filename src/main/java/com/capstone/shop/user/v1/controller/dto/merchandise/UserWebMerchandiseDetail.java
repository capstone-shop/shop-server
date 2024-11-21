package com.capstone.shop.user.v1.controller.dto.merchandise;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserWebMerchandiseDetail {
    private UserWebMerchandise merchandise;
    private List<UserWebMerchandise> relatedMerchandises;
}
