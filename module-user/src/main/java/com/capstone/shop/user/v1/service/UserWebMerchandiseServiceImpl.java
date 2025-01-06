package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.dto.ApiResponse;
import com.capstone.shop.core.domain.dto.CreateApiResponse;
import com.capstone.shop.core.domain.entity.Category;
import com.capstone.shop.core.domain.entity.Merchandise;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.entity.Wish;
import com.capstone.shop.core.domain.repository.CategoryRepository;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.core.domain.repository.WishRepository;
import com.capstone.shop.core.domain.repository.merchandise.MerchandiseQueryRepository;
import com.capstone.shop.core.domain.repository.merchandise.MerchandiseRepository;
import com.capstone.shop.core.domain.repository.merchandise.MerchandiseSpec;
import com.capstone.shop.user.v1.controller.dto.home.HomeMerchandiseList;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandise;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseDetail;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandisePagination;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseRegister;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebWish;
import com.capstone.shop.core.util.search.Filter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private final MerchandiseQueryRepository merchandiseQueryRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    @Override
    public UserWebMerchandisePagination getMerchandise(String search, Pageable pageable, Filter filter) {
        Specification<Merchandise> spec = MerchandiseSpec
                .builder()
                .addFilterCriteria(filter)
                .addSearchString(search)
                .build();

        Page<Merchandise> result = merchandiseRepository.findAll(spec, pageable);

        return new UserWebMerchandisePagination(result);
    }

    @Override
    public UserWebMerchandiseDetail getMerchandise(Long merchandiseId) {

        Merchandise merchandise = merchandiseRepository.findById(merchandiseId).orElseThrow();
        List<UserWebMerchandise> list = merchandiseQueryRepository.findRelatedMerchandises(merchandise)
                .stream()
                .map(UserWebMerchandise::new)
                .toList();

        merchandise.addViewCount();
        merchandise = merchandiseRepository.save(merchandise);

        return new UserWebMerchandiseDetail(new UserWebMerchandise(merchandise), list);
    }

    @Override
    public CreateApiResponse createMerchandise(UserWebMerchandiseRegister request, Long id) {
        // 추후 현재 로그인한 유저를 가져와야 함.
        User user = User.builder().id(id).build();

        Optional<Category> categoryOp = categoryRepository.findById(Long.valueOf(request.getCategoryId()));
        if (categoryOp.isEmpty())
            return new CreateApiResponse(false, "상품 등록 : 카테고리가 존재하지 않음");

        Category category = categoryOp.get();
        Merchandise entity = request.toEntity(category, user);

        entity = merchandiseRepository.save(entity);
        return new CreateApiResponse(true, "상품 등록 : 성공", entity.getId());
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
    public UserWebWish toggleWish(Long id, Long userId){
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없어요"));
        User user = User.builder().id(userId).build();

        Optional<Wish> existingWish = wishRepository.findByUserAndMerchandise(user, merchandise);

        if (existingWish.isPresent()) {//이미 찜을 했다면?
            merchandise.subWishCount();    //찜 목록이랑 횟수 감소
            wishRepository.delete(existingWish.get()); //찜 테이블에서 삭제
        } else {
            merchandise.addWishCount(); //아니라면 상품 찜 갯수에 1 추가하고
            Wish newWish = Wish.builder()
                    .user(user)
                    .merchandise(merchandise)
                    .wishDate(LocalDateTime.now())
                    .build();
            wishRepository.save(newWish); //찜 테이블에 추가
        }
        merchandiseRepository.save(merchandise);

        return new UserWebWish(id, existingWish.isEmpty());
    }

    @Override
    public UserWebWish wishCount(Long id, Long userId) {
        Merchandise merchandise = merchandiseRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없어요"));
        User user = User.builder().id(userId).build();

        Optional<Wish> existingWish = wishRepository.findByUserAndMerchandise(user, merchandise);
        return new UserWebWish(id, existingWish.isPresent());
    }

    @Override
    public ApiResponse deleteMerchandise(Long id, Long merchandiseId) {
        Optional<Merchandise> merchandise = merchandiseRepository.findById(merchandiseId);
        if (merchandise.isEmpty()) {
            return new ApiResponse(false, "해당하는 id의 상품이 없습니다.");
        }
        if (!Objects.equals(merchandise.get().getRegister().getId(), id)) {
            return new ApiResponse(false, "현재 사용자 계정에서 등록된 상품이 아닙니다.");
        }
        merchandiseRepository.delete(merchandise.get());
        return new ApiResponse(true, "삭제 완료");
    }

    @Override
    public ApiResponse putMerchandise(Long id, Long merchandiseId, UserWebMerchandiseRegister request) {
        Optional<Merchandise> merchandise = merchandiseRepository.findById(merchandiseId);
        if (merchandise.isEmpty()) {
            return new ApiResponse(false, "해당하는 id의 상품이 없습니다.");
        }
        if (!Objects.equals(merchandise.get().getRegister().getId(), id)) {
            return new ApiResponse(false, "현재 사용자 계정에서 등록된 상품이 아닙니다.");
        }

        Optional<Category> categoryOp = categoryRepository.findById(Long.valueOf(request.getCategoryId()));
        if (categoryOp.isEmpty()) {
            return new ApiResponse(false, "해당하는 id의 카테고리가 없습니다.");
        }

        Optional<User> userOp = userRepository.findById(id);
        if (userOp.isEmpty()) {
            return new ApiResponse(false, "사용자 계정이 조회되지 않습니다.");
        }

        merchandiseRepository.save(request.toEntity(categoryOp.get(), userOp.get(), merchandiseId));
        return new ApiResponse(true, "수정 완료");
    }
}
