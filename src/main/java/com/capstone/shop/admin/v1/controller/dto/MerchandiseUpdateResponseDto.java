package com.capstone.shop.admin.v1.controller.dto;


import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.enums.MerchandiseQualityState;
import com.capstone.shop.enums.MerchandiseSaleState;
import com.capstone.shop.enums.TransactionMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchandiseUpdateResponseDto {
    private String name;
    private String description;
    private int price;
    private String categoryName; // 카테고리 이름
    private MerchandiseSaleState saleState;
    private MerchandiseQualityState merchandiseState;
    private List<String> images; // 이미지 URL 목록
    private String location; // 판매 지역
    private boolean negotiationAvailable; // 가격 협상 가능 여부
    private TransactionMethod transactionMethod; // 거래 방식
    private String registerName; // 등록자 이름

    public static MerchandiseUpdateResponseDto fromEntity(Merchandise merchandise) {
        return MerchandiseUpdateResponseDto.builder()
                .name(merchandise.getName())
                .description(merchandise.getDescription())
                .price(merchandise.getPrice())
                .categoryName(merchandise.getCategory().getTitle()) // Category의 이름 가져오기
                .saleState(merchandise.getSaleState())
                .merchandiseState(merchandise.getMerchandiseState())
                .images(merchandise.getImages()) // 이미지 URL을 List<String> 형태로 반환
                .location(merchandise.getLocation())
                .negotiationAvailable(merchandise.isNegotiationAvailable())
                .transactionMethod(merchandise.getTransactionMethod())
                .registerName(merchandise.getRegister().getName()) // 등록자 이름 가져오기
                .build();
    }
}
