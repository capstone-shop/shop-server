package com.capstone.shop.user.v1.dto;

import com.capstone.shop.entity.Image;
import com.capstone.shop.entity.Merchandise;
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
    private int price;
    private String location;
    private int like;
    private List<String> images;

    public MerchandiseResponse(Merchandise entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.location = entity.getLocation();
        this.like = entity.getLike();
        this.images = entity.getImages()
                .stream()
                .map(Image::getPictureUrl)
                .toList();
    }

    public static List<MerchandiseResponse> getMerchandiseResponses(Page<Merchandise> merchandisePage) {
        return merchandisePage
                .getContent()
                .stream()
                .map(MerchandiseResponse::new)
                .toList();
    }
}
