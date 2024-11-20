package com.capstone.shop.entity;

import com.capstone.shop.enums.MerchandiseQualityState;
import com.capstone.shop.enums.MerchandiseSaleState;
import com.capstone.shop.enums.TransactionMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Merchandise extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "register_id", nullable = false)
    private User register; // 등록자 User 객체와 연결

    @Column(columnDefinition = "TEXT")
    private String description; // 상품 설명

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchandiseSaleState saleState; // 판매 여부 (판매, 예약, 판매완료)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchandiseQualityState merchandiseState; // 상품 상태 (NEW, GOOD, AVERAGE, BAD, BROKEN)

    @Column(name = "image_urls")
    private String imageUrls; // 이미지 URL (;로 구분)

    @Column(nullable = false)
    private int view; // 조회수

    @Column(nullable = false)
    private int wish; // 찜 횟수

    @Column(nullable = false)
    private int chat; // 대화 횟수

    @Column(nullable = false)
    private String location; // 판매 지역

    @Column(name = "negotiation_available", nullable = false)
    private boolean negotiationAvailable; // 가격 협상 가능 여부

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_method", nullable = false)
    private TransactionMethod transactionMethod; // 거래 방식 (직거래, 택배, 둘 다 가능)

    public List<String> getImages() {
        return Arrays.stream(imageUrls.split(";")).toList();
    }
}