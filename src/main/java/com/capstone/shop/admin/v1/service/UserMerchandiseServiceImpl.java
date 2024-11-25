package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.MerchandiseUpdateRequestDto;
import com.capstone.shop.admin.v1.repository.CategoryRepository;
import com.capstone.shop.entity.Category;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.entity.User;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMerchandiseServiceImpl implements UserMerchandiseService{
    private final UserWebMerchandiseRepository merchandiseRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public ApiResponse updateMerchandise(MerchandiseUpdateRequestDto merchandiseUpdateRequestDto, Long id) {
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없음."));


        Category category = categoryRepository.findByTitle(merchandiseUpdateRequestDto.getCategoryTitle())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없음."));


        Merchandise updatedMerchandise = Merchandise.builder()
                .id(merchandise.getId())
                .view(merchandise.getView())
                .wish(merchandise.getWish())
                .chat(merchandise.getChat())
                .name(merchandiseUpdateRequestDto.getName())
                .description(merchandiseUpdateRequestDto.getDescription())
                .price(merchandiseUpdateRequestDto.getPrice())
                .category(category)
                .saleState(merchandiseUpdateRequestDto.getSaleState())
                .merchandiseState(merchandiseUpdateRequestDto.getMerchandiseState())
                .imageUrls(String.join(";", merchandiseUpdateRequestDto.getImageUrls()))
                .location(merchandiseUpdateRequestDto.getLocation())
                .negotiationAvailable(merchandiseUpdateRequestDto.isNegotiationAvailable())
                .transactionMethod(merchandiseUpdateRequestDto.getTransactionMethod())
                .register(merchandise.getRegister())
                .build();

        merchandiseRepository.save(updatedMerchandise);

        return new ApiResponse(true, "상품 정보 수정 성공");
    }

    @Override
    public ApiResponse deleteMerchandise(Long id) {
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없음."));
        merchandiseRepository.delete(merchandise);
        return new ApiResponse(true, "상품 삭제 성공");
    }
}
