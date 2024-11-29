package com.capstone.shop.admin.v1.controller.dto;


import com.capstone.shop.core.domain.enums.MerchandiseQualityState;
import com.capstone.shop.core.domain.enums.MerchandiseSaleState;
import com.capstone.shop.core.domain.enums.TransactionMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchandiseUpdateRequestDto {
    private String name;
    private String description;
    private int price;
    private String categoryTitle; // 카테고리 이름
    private MerchandiseSaleState saleState;
    private MerchandiseQualityState merchandiseState;
    private String imageUrls ; // 이미지 URL 목록
    private String location; // 판매 지역
    private boolean negotiationAvailable; // 가격 협상 가능 여부
    private TransactionMethod transactionMethod; // 거래 방식
    private String registerName; // 등록자 이름

}
