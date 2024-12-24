package com.capstone.shop.user.v1.controller.dto.merchandise;

import com.capstone.shop.core.domain.dto.PaginationQueryResult;
import com.capstone.shop.core.domain.entity.Merchandise;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class UserWebMerchandisePagination {
    private final List<UserWebMerchandise> merchandise;
    private final int totalPage;

    public UserWebMerchandisePagination(List<Merchandise> entityList, int totalPage) {
        merchandise = UserWebMerchandise.entityListToDtoList(entityList);
        this.totalPage = totalPage;
    }

    public UserWebMerchandisePagination(PaginationQueryResult<Merchandise> entityPage) {
        merchandise = UserWebMerchandise.entityListToDtoList(entityPage.getData());
        this.totalPage = (int) entityPage.getTotalPage();
    }

    public UserWebMerchandisePagination(Page<Merchandise> entityPage) {
        merchandise = UserWebMerchandise.entityPageToDtoList(entityPage);
        totalPage = entityPage.getTotalPages();
    }
}