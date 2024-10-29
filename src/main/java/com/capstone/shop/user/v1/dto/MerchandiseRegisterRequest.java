package com.capstone.shop.user.v1.dto;

import com.capstone.shop.entity.Merchandise;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class MerchandiseRegisterRequest {
    private String name;
    private int price;
    private String location;
    private List<String> images;

    public Merchandise toEntity() {
        return Merchandise.builder()
                .name(name)
                .price(price)
                .location(location)
                .build();
    }
}
