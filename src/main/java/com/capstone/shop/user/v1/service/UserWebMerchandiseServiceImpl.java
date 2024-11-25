package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.Category;
import com.capstone.shop.entity.User;
import com.capstone.shop.entity.Wish;
import com.capstone.shop.security.CurrentUser;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandise;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.user.v1.dto.ApiResponse;
import com.capstone.shop.user.v1.repository.UserWebCategoryRepository;
import com.capstone.shop.user.v1.repository.WishRepository;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseQueryRepository;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.repository.merchandise.UserWebMerchandiseSpec;
import com.capstone.shop.user.v1.search.Filter;

import java.time.LocalDateTime;
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
    private final WishRepository wishRepository;

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

        return new UserWebMerchandiseDetail(new UserWebMerchandise(merchandise), list);
    }

    @Override
    public boolean createMerchandise(UserWebMerchandiseRegister request) {
        // 추후 현재 로그인한 유저를 가져와야 함.
        User user = User.builder().id(1L).build();

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

    @Override
    public ApiResponse WishCount(Long id, User user){
        Merchandise merchandise = userWebMerchandiseRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없어요"));
        Optional<Wish> existingWish = wishRepository.findByUserAndMerchandise(user, merchandise);
        if (existingWish.isPresent()) {//이미 찜을 했다면?
            merchandise.subWishCount();    //찜 목록이랑 횟수 감소
            wishRepository.delete(existingWish.get());
        } else{
            merchandise.addWishCount(); //아니라면 추가
        }

        Wish newWish = Wish.builder()
                .user(user)
                .merchandise(merchandise)
                .wishDate(LocalDateTime.now())  //찜 날짜임
                .build();

        wishRepository.save(newWish);
        userWebMerchandiseRepository.save(merchandise);

        return new ApiResponse(true, "찜 횟수 증가 및 위시리스트에 추가 성공");

    }

}
