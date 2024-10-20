package com.capstone.shop.user.v1.service;

import com.capstone.shop.entity.Merchandise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MerchandiseService {

    Page<Merchandise> getMerchandise(String search, Pageable pageable);

    boolean createMerchandise(Merchandise entity);
}