package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.repository.MerchandiseRepository;
import com.capstone.shop.entity.Merchandise;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchandiseServiceImpl implements MerchandiseService {
    private final MerchandiseRepository merchandiseRepository;

    @Override
    public Page<Merchandise> getMerchandise(String search, Pageable pageable) {
        return merchandiseRepository.findByNameContaining(search, pageable);
    }

    @Override
    public boolean createMerchandise(Merchandise entity) {
        entity = merchandiseRepository.save(entity);
        return entity.getId() != null;
    }
}
