package com.capstone.shop.user.v1.repository;

import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.user.v1.util.Filter;
import com.capstone.shop.user.v1.util.Filter.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.domain.Specification;

public class UserWebMerchandiseSpec {

    private Specification<Merchandise> spec;

    private UserWebMerchandiseSpec() {
        spec = (root, query, builder) -> null;
    }

    public UserWebMerchandiseSpec isOnSale() {
        spec = spec.and((root, query, builder) -> builder.equal(root.get("saleState"), "SALE"));
        return this;
    }

    public UserWebMerchandiseSpec isRegisteredInLast2Weeks() {
        LocalDate localDate = LocalDate.now().minusWeeks(2);
        Date date = new Date(localDate.toEpochDay());
        spec = spec.and(((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("createdAt"), date)));
        return this;
    }

    public UserWebMerchandiseSpec addFilterCriteria(Filter filter) {
        for (FilterType filterType : FilterType.values()) {
            List<FilterOption> options = filter.getFilterOptions(filterType);
            spec = spec.and(addFilter(filterType, options));
        }
        return this;
    }

    private Specification<Merchandise> addFilter(FilterType filterType, List<FilterOption> options) {
        return (root, query, builder) -> {
            if (options == null) {
                return null;
            }

            String column = filterType.getColumnName();
            QueryType queryType = filterType.getQuery();
            Path path;

            String[] columns = column.split("\\.");
            if (columns.length == 1)
                path = root.get(columns[columns.length - 1]);
            else {
                Join join = root.join(columns[0]);
                for (int i = 1; i < columns.length-1; i++) {
                    join = join.join(columns[i]);
                }
                path = join.get(columns[columns.length - 1]);
            }

            Stream<FilterOption> stream = options.stream();
            Stream<Predicate> predicateStream = switch (queryType) {
                case EQUAL -> stream.map(option -> builder.equal(path, option.getValue()));
                case GREATER_OR_EQUAL -> stream.map(option -> builder.greaterThanOrEqualTo(path, option.getIntValue()));
            };
            return builder.or(predicateStream.toArray(Predicate[]::new));
        };
    }

    public static UserWebMerchandiseSpec builder() {
        return new UserWebMerchandiseSpec();
    }

    public Specification<Merchandise> build() {
        return spec;
    }
}
