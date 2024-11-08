package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseListAndPaginationResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseResponse;
import com.capstone.shop.user.v1.repository.MerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.repository.MerchandiseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchandiseServiceImpl implements MerchandiseService {
    private final MerchandiseRepository merchandiseRepository;

    @Override
    public MerchandiseListAndPaginationResponse getMerchandise(String sort, String search, Pageable pageable) {
        var result = merchandiseRepository.findByNameContaining(search, pageable);

        var merchandiseList = MerchandiseResponse.entityPageToDtoList(result);

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
    public HomeMerchandiseList getHomeMerchandiseList() {
        var spec = MerchandiseSpec
                .builder()
                .isOnSale()
                .isRegisteredInLast2Weeks()
                .build();

        var top3OrderByCreatedAt = PageRequest.of(0, 3, Direction.DESC, "createdAt");
        var top3OrderByView = PageRequest.of(0, 3, Direction.DESC, "view");

        var recentlyRegistered = merchandiseRepository.findAll(spec, top3OrderByCreatedAt);
        var recentlyViewed = merchandiseRepository.findAll(spec, top3OrderByView);

        var recentlyRegisteredList = MerchandiseResponse.entityPageToDtoList(recentlyRegistered);
        var recentlyViewedList = MerchandiseResponse.entityPageToDtoList(recentlyViewed);

        return new HomeMerchandiseList(recentlyRegisteredList, recentlyViewedList);
    }
}
