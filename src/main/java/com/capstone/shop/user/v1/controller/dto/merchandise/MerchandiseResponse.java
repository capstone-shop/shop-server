package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.entity.Merchandise;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
public class MerchandiseResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String location;
    private boolean negotiationAvailable;
    private String transactionMethod;
    private int view;
    private int wish;
    private int chat;
    private String images;
    private LocalDateTime createdAt;

    public MerchandiseResponse(Merchandise entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.location = entity.getLocation();
        this.negotiationAvailable = entity.isNegotiationAvailable();
        this.transactionMethod = entity.getTransactionMethod().name();
        this.view = entity.getView();
        this.wish = entity.getWish();
        this.chat = entity.getChat();
        this.images = entity.getImageUrls();
        this.createdAt = entity.getCreatedAt();
    }

    public static List<MerchandiseResponse> getMerchandiseResponses(Page<Merchandise> merchandisePage) {
        return merchandisePage
                .getContent()
                .stream()
                .map(MerchandiseResponse::new)
                .toList();
    }
}