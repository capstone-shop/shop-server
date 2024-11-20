package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandise;
import com.capstone.shop.user.v1.repository.UserWebMerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.repository.UserWebMerchandiseSpec;
import com.capstone.shop.user.v1.search.Filter;
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
public class UserWebMerchandiseServiceImpl implements UserWebMerchandiseService {

    private final UserWebMerchandiseRepository userWebMerchandiseRepository;

    @Override
    public UserWebMerchandisePagination getMerchandise(String search, Pageable pageable, Filter filter) {
        Specification<Merchandise> spec = UserWebMerchandiseSpec
                .builder()
                .addFilterCriteria(filter)
                .addSearchString(search)
                .build();

        Page<Merchandise> result = userWebMerchandiseRepository.findAll(spec, pageable);

        List<UserWebMerchandise> merchandiseList = UserWebMerchandise.entityPageToDtoList(result);

        return new UserWebMerchandisePagination(merchandiseList, result.getTotalPages());
    }

    @Override
    public boolean createMerchandise(Merchandise entity) {
        entity = userWebMerchandiseRepository.save(entity);
        return entity.getId() != null;
    }

    @Override
    public HomeMerchandiseList getHomeMerchandiseList() {
        Specification<Merchandise> spec = UserWebMerchandiseSpec
                .builder()
                .isOnSale()
                .isRegisteredInLast2Weeks()
                .build();

        Pageable top3OrderByCreatedAt = PageRequest.of(0, 3, Direction.DESC, "createdAt");
        Pageable top3OrderByView = PageRequest.of(0, 3, Direction.DESC, "view");

        Page<Merchandise> recentlyRegistered = userWebMerchandiseRepository.findAll(spec, top3OrderByCreatedAt);
        Page<Merchandise> recentlyViewed = userWebMerchandiseRepository.findAll(spec, top3OrderByView);

        List<UserWebMerchandise> recentlyRegisteredList = UserWebMerchandise.entityPageToDtoList(recentlyRegistered);
        List<UserWebMerchandise> recentlyViewedList = UserWebMerchandise.entityPageToDtoList(recentlyViewed);

        return new HomeMerchandiseList(recentlyRegisteredList, recentlyViewedList);
    }
}
