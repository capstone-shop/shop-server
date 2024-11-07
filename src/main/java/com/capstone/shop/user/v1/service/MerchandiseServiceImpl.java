package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseListAndPaginationResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseResponse;
import com.capstone.shop.user.v1.repository.MerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchandiseServiceImpl implements MerchandiseService {
    private final MerchandiseRepository merchandiseRepository;

    @Override
    public MerchandiseListAndPaginationResponse getMerchandise(String sort, String search, Pageable pageable) {
        var result = merchandiseRepository.findByNameContaining(search, pageable);

        var merchandiseList = result
                .getContent()
                .stream()
                .map(MerchandiseResponse::new)
                .toList();

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        var paginationResponse = new PaginationResponse(page, size, sort, search, result);

        return new MerchandiseListAndPaginationResponse(merchandiseList, paginationResponse);
    }

    @Override
    public boolean createMerchandise(Merchandise entity) {
        entity = merchandiseRepository.save(entity);
        return entity.getId() != null;
    }

    @Override
    public HomeMerchandiseList getRecentlyViewedAndRecentlyRegisteredMerchandise() {
        var recentlyRegistered = merchandiseRepository.findTop3ByOrderByCreatedAtDesc();
        var recentlyViewed = merchandiseRepository.findTop3ByOrderByModifiedAtDesc();

        var recentlyRegisteredList = recentlyRegistered
                .stream()
                .map(MerchandiseResponse::new)
                .toList();
        var recentlyViewedList = recentlyViewed
                .stream()
                .map(MerchandiseResponse::new)
                .toList();

        return new HomeMerchandiseList(recentlyRegisteredList, recentlyViewedList);
    }
}
