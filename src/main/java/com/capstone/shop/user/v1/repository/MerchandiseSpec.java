package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.Merchandise;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.data.jpa.domain.Specification;

public class MerchandiseSpec {
    private Specification<Merchandise> spec;

    private MerchandiseSpec() {
        spec = (root, query, builder) -> null;
    }

    public MerchandiseSpec isOnSale() {
        spec = spec.and((root, query, builder) -> builder.equal(root.get("saleState"), "SALE"));
        return this;
    }

    public MerchandiseSpec isRegisteredInLast2Weeks() {
        LocalDate localDate = LocalDate.now().minusWeeks(2);
        Date date = new Date(localDate.toEpochDay());
        spec = spec.and(((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("createdAt"), date)));
        return this;
    }

    public static MerchandiseSpec builder() {
        return new MerchandiseSpec();
    }

    public Specification<Merchandise> build() {
        return spec;
    }
}
