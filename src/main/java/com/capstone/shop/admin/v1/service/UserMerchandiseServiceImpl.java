package com.capstone.shop.admin.v1.service;

import com.capstone.shop.admin.v1.controller.dto.MerchandiseUpdateResponseDto;
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
    @Override
    public ApiResponse updateMerchandise(MerchandiseUpdateResponseDto merchandiseUpdateResponseDto, Long id) {
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없음."));

        Category category = merchandise.getCategory(); // 기존 카테고리
        User register = merchandise.getRegister(); // 기존 등록자
        Merchandise updatedMerchandise = Merchandise.toEntity(merchandiseUpdateResponseDto, category, register);

        // 기존 ID, 조회수, 찜, 대화 횟수 유지
        updatedMerchandise = Merchandise.builder()
                .id(merchandise.getId())
                .view(merchandise.getView())
                .wish(merchandise.getWish())
                .chat(merchandise.getChat())
                .build();
        merchandiseRepository.save(updatedMerchandise);

        return new ApiResponse(true, "카테고리 수정 성공");
    }

    @Override
    public ApiResponse deleteMerchandise(Long id) {
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없음."));
        merchandiseRepository.delete(merchandise);
        return new ApiResponse(true, "상품 삭제 성공");
    }
}
