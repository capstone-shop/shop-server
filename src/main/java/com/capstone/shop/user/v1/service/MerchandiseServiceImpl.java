package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.PaginationResponse;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseListAndPaginationResponse;
import com.capstone.shop.user.v1.controller.dto.merchandise.MerchandiseResponse;
import com.capstone.shop.user.v1.repository.MerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.repository.MerchandiseSpec;
import com.capstone.shop.user.v1.util.Filter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchandiseServiceImpl implements MerchandiseService {
    private final MerchandiseRepository merchandiseRepository;

    @Override
    public MerchandiseListAndPaginationResponse getMerchandise(String sort, String search, Pageable pageable,
            Filter filter) {
        Specification<Merchandise> spec = MerchandiseSpec
                .builder()
                .addFilterCriteria(filter)
                .build();

        Page<Merchandise> result = merchandiseRepository.findAll(spec, pageable);

        List<MerchandiseResponse> merchandiseList = MerchandiseResponse.entityPageToDtoList(result);

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
        Specification<Merchandise> spec = MerchandiseSpec
                .builder()
                .isOnSale()
                .isRegisteredInLast2Weeks()
                .build();

        Pageable top3OrderByCreatedAt = PageRequest.of(0, 3, Direction.DESC, "createdAt");
        Pageable top3OrderByView = PageRequest.of(0, 3, Direction.DESC, "view");

        Page<Merchandise> recentlyRegistered = merchandiseRepository.findAll(spec, top3OrderByCreatedAt);
        Page<Merchandise> recentlyViewed = merchandiseRepository.findAll(spec, top3OrderByView);

        List<MerchandiseResponse> recentlyRegisteredList = MerchandiseResponse.entityPageToDtoList(recentlyRegistered);
        List<MerchandiseResponse> recentlyViewedList = MerchandiseResponse.entityPageToDtoList(recentlyViewed);

        return new HomeMerchandiseList(recentlyRegisteredList, recentlyViewedList);
    }
}
