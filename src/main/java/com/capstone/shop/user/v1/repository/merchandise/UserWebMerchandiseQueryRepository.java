package com.capstone.shop.user.v1.repository.merchandise;

import com.capstone.shop.entity.Merchandise;
import java.util.List;

public interface UserWebMerchandiseQueryRepository {
    List<Merchandise> findRelatedMerchandises(Merchandise merchandise);
}
