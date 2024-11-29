package com.capstone.shop.core.domain.repository.merchandise;

import com.capstone.shop.core.domain.entity.Merchandise;

import java.util.List;

public interface MerchandiseQueryRepository {
    List<Merchandise> findRelatedMerchandises(Merchandise merchandise);
}
