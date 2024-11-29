package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.core.domain.entity.Merchandise;
import com.capstone.shop.core.domain.enums.MerchandiseQualityState;
import com.capstone.shop.core.domain.enums.MerchandiseSaleState;
import com.capstone.shop.user.v1.controller.dto.category.UserWebCategory;
import com.capstone.shop.user.v1.controller.dto.user.UserWebSeller;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
public class UserWebMerchandise {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String location;
    private List<UserWebCategory> category;
    private UserWebSeller register;
    private MerchandiseSaleState saleState;
    private MerchandiseQualityState merchandiseState;
    private boolean negotiationAvailable;
    private String transactionMethod;
    private int view;
    private int wish;
    private int chat;
    private List<String> images;
    private LocalDateTime createdAt;

    public UserWebMerchandise(Merchandise entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.location = entity.getLocation();
        this.category = UserWebCategory.entityListToDtoList(entity.getCategory().getCategoryList());
        this.register = new UserWebSeller(entity.getRegister());
        this.saleState = entity.getSaleState();
        this.merchandiseState = entity.getMerchandiseState();
        this.negotiationAvailable = entity.isNegotiationAvailable();
        this.transactionMethod = entity.getTransactionMethod().name();
        this.view = entity.getView();
        this.wish = entity.getWish();
        this.chat = entity.getChat();
        this.images = entity.getImages();
        this.createdAt = entity.getCreatedAt();
    }

    public static List<UserWebMerchandise> entityPageToDtoList(Page<Merchandise> merchandisePage) {
        return entityListToDtoList(merchandisePage.getContent());
    }

    public static List<UserWebMerchandise> entityListToDtoList(List<Merchandise> merchandisePage) {
        return merchandisePage
                .stream()
                .map(UserWebMerchandise::new)
                .toList();
    }
}