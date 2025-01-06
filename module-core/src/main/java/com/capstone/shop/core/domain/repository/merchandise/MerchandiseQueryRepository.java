package com.capstone.shop.core.domain.repository.merchandise;

import com.capstone.shop.core.domain.dto.PaginationQueryResult;
import com.capstone.shop.core.domain.entity.Merchandise;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MerchandiseQueryRepository {
    List<Merchandise> findRelatedMerchandises(Merchandise merchandise);

    PaginationQueryResult<Merchandise> findWishlistOrderByWishDate(Long id, Pageable page);

    PaginationQueryResult<Merchandise> findRegisteredMerchandiseOrderByCreatedAt(Long id, Pageable page);
}
