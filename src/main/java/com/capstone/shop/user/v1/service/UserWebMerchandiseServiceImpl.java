package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.Category;
import com.capstone.shop.entity.User;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandise;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.user.v1.repository.UserWebCategoryRepository;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseQueryRepository;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseSpec;
import com.capstone.shop.user.v1.search.Filter;
import java.util.List;
import java.util.Optional;
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
    private final UserWebCategoryRepository userWebCategoryRepository;
    private final UserWebMerchandiseQueryRepository userWebMerchandiseQueryRepository;

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
    public UserWebMerchandiseDetail getMerchandise(Long merchandiseId) {

        Merchandise merchandise = userWebMerchandiseRepository.findById(merchandiseId).orElseThrow();
        List<UserWebMerchandise> list = userWebMerchandiseQueryRepository.findRelatedMerchandises(merchandise)
                .stream()
                .map(UserWebMerchandise::new)
                .toList();

        merchandise.addViewCount();
        merchandise = userWebMerchandiseRepository.save(merchandise);

        return new UserWebMerchandiseDetail(new UserWebMerchandise(merchandise), list);
    }

    @Override
    public boolean createMerchandise(UserWebMerchandiseRegister request, Long id) {
        // 추후 현재 로그인한 유저를 가져와야 함.
        User user = User.builder().id(id).build();

        Optional<Category> categoryOp = userWebCategoryRepository.findById(Long.valueOf(request.getCategoryId()));
        if (categoryOp.isEmpty())
            return false;

        Category category = categoryOp.get();
        Merchandise entity = request.toEntity(category, user);

        entity = userWebMerchandiseRepository.save(entity);
        return entity.getId() != null;
    }

    @Override
    public HomeMerchandiseList getHomeMerchandiseList() {
        Specification<Merchandise> spec = UserWebMerchandiseSpec
                .builder()
                .isOnSale()
                .build();

        Pageable top3OrderByCreatedAt = PageRequest.of(0, 4, Direction.DESC, "createdAt");
        Pageable top3OrderByView = PageRequest.of(0, 4, Direction.DESC, "wish");

        Page<Merchandise> recentlyRegistered = userWebMerchandiseRepository.findAll(spec, top3OrderByCreatedAt);
        Page<Merchandise> mostWished = userWebMerchandiseRepository.findAll(spec, top3OrderByView);

        List<UserWebMerchandise> recentlyRegisteredList = UserWebMerchandise.entityPageToDtoList(recentlyRegistered);
        List<UserWebMerchandise> mostWishedList = UserWebMerchandise.entityPageToDtoList(mostWished);

        return new HomeMerchandiseList(recentlyRegisteredList, mostWishedList);
    }
}
