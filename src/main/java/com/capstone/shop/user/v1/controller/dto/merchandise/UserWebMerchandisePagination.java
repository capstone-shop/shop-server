package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.core.domain.entity.Merchandise;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Getter
public class UserWebMerchandisePagination {
    private List<UserWebMerchandise> merchandise;
    private Integer totalPage;

    public UserWebMerchandisePagination(List<Merchandise> entityList) {
        merchandise = UserWebMerchandise.entityListToDtoList(entityList);
        totalPage = entityList.size();
    }

    public UserWebMerchandisePagination(Page<Merchandise> entityPage) {
        merchandise = UserWebMerchandise.entityPageToDtoList(entityPage);
        totalPage = entityPage.getTotalPages();
    }
}