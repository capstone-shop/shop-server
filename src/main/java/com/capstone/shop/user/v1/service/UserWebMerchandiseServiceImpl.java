package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.Category;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.entity.Wish;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandise;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.repository.CategoryRepository;
import com.capstone.shop.core.domain.repository.WishRepository;
import com.capstone.shop.core.domain.repository.merchandise.MerchandiseQueryRepository;
import com.capstone.shop.core.domain.repository.merchandise.MerchandiseRepository;
import com.capstone.shop.core.domain.entity.Merchandise;
import com.capstone.shop.core.domain.repository.merchandise.MerchandiseSpec;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebWish;
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

    private final MerchandiseRepository merchandiseRepository;
    private final CategoryRepository categoryRepository;
    private final MerchandiseQueryRepository userWebMerchandiseQueryRepository;
    private final WishRepository wishRepository;

    @Override
    public UserWebMerchandisePagination getMerchandise(String search, Pageable pageable, Filter filter) {
        Specification<Merchandise> spec = MerchandiseSpec
                .builder()
                .addFilterCriteria(filter)
                .addSearchString(search)
                .build();

        Page<Merchandise> result = merchandiseRepository.findAll(spec, pageable);

        List<UserWebMerchandise> merchandiseList = UserWebMerchandise.entityPageToDtoList(result);

        return new UserWebMerchandisePagination(merchandiseList, result.getTotalPages());
    }

    @Override
    public UserWebMerchandiseDetail getMerchandise(Long merchandiseId) {

        Merchandise merchandise = merchandiseRepository.findById(merchandiseId).orElseThrow();
        List<UserWebMerchandise> list = userWebMerchandiseQueryRepository.findRelatedMerchandises(merchandise)
                .stream()
                .map(UserWebMerchandise::new)
                .toList();

        merchandise.addViewCount();
        merchandise = merchandiseRepository.save(merchandise);

        return new UserWebMerchandiseDetail(new UserWebMerchandise(merchandise), list);
    }

    @Override
    public boolean createMerchandise(UserWebMerchandiseRegister request, Long id) {
        // 추후 현재 로그인한 유저를 가져와야 함.
        User user = User.builder().id(id).build();

        Optional<Category> categoryOp = categoryRepository.findById(Long.valueOf(request.getCategoryId()));
        if (categoryOp.isEmpty())
            return false;

        Category category = categoryOp.get();
        Merchandise entity = request.toEntity(category, user);

        entity = merchandiseRepository.save(entity);
        return entity.getId() != null;
    }

    @Override
    public HomeMerchandiseList getHomeMerchandiseList() {
        Specification<Merchandise> spec = MerchandiseSpec
                .builder()
                .isOnSale()
                .build();

        Pageable top3OrderByCreatedAt = PageRequest.of(0, 4, Direction.DESC, "createdAt");
        Pageable top3OrderByView = PageRequest.of(0, 4, Direction.DESC, "wish");

        Page<Merchandise> recentlyRegistered = merchandiseRepository.findAll(spec, top3OrderByCreatedAt);
        Page<Merchandise> mostWished = merchandiseRepository.findAll(spec, top3OrderByView);

        List<UserWebMerchandise> recentlyRegisteredList = UserWebMerchandise.entityPageToDtoList(recentlyRegistered);
        List<UserWebMerchandise> mostWishedList = UserWebMerchandise.entityPageToDtoList(mostWished);

        return new HomeMerchandiseList(recentlyRegisteredList, mostWishedList);
    }

    @Override
    public ApiResponse toggleWish(Long id, Long userId){
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없어요"));
        User user = User.builder().id(userId).build();

        Optional<Wish> existingWish = wishRepository.findByUserAndMerchandise(user, merchandise);

        String msg;

        if (existingWish.isPresent()) {//이미 찜을 했다면?
            merchandise.subWishCount();    //찜 목록이랑 횟수 감소
            wishRepository.delete(existingWish.get()); //찜 테이블에서 삭제
            msg = "찜 횟수 감소 및 위시리스트에서 삭제 성공";
        } else {
            merchandise.addWishCount(); //아니라면 상품 찜 갯수에 1 추가하고
            Wish newWish = Wish.builder()
                    .user(user)
                    .merchandise(merchandise)
                    .wishDate(LocalDateTime.now())
                    .build();
            wishRepository.save(newWish); //찜 테이블에 추가
            msg = "찜 횟수 증가 및 위시리스트에 추가 성공";
        }
        merchandiseRepository.save(merchandise);

        return new ApiResponse(true, msg);
    }

    @Override
    public UserWebWish wishCount(Long id, Long userId) {
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없어요"));
        User user = User.builder().id(userId).build();

        Optional<Wish> existingWish = wishRepository.findByUserAndMerchandise(user, merchandise);
        return new UserWebWish(id, existingWish.isPresent());
    }

}
