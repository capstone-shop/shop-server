package com.capstone.shop.user.v1.controller.dto.home;

import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandise;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HomeMerchandiseList {
    private List<UserWebMerchandise> recentlyRegistered;
    private List<UserWebMerchandise> recentlyViewed;
}
