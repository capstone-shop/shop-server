package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.entity.Category;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.entity.User;
import com.capstone.shop.enums.MerchandiseQualityState;
import com.capstone.shop.enums.MerchandiseSaleState;
import com.capstone.shop.enums.TransactionMethod;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserWebMerchandiseRegister {

    private List<String> images;
    private String name;
    private Integer categoryId;
    private MerchandiseQualityState state;
    private String description;
    private int price;
    private Boolean direct;
    private Boolean nego;
    private Boolean delivery;
    private String location;

    public Merchandise toEntity(Category cate, User user) {
        TransactionMethod transactionMethod = TransactionMethod.DELIVERY;
        if (direct && delivery)
            transactionMethod = TransactionMethod.BOTH;
        else if (direct)
            transactionMethod = TransactionMethod.DIRECT;

        return Merchandise.builder()
                .name(name)
                .register(user)
                .description(description)
                .price(price)
                .category(cate)
                .saleState(MerchandiseSaleState.SALE)
                .merchandiseState(state)
                .imageUrls(images.stream().reduce(String::concat).orElse(""))
                .location(location)
                .negotiationAvailable(nego)
                .transactionMethod(transactionMethod)
                .build();
    }
}
