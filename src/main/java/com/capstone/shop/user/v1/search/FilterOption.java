package com.capstone.shop.user.v1.search;

import java.util.List;

public interface FilterOption {
    Object getValue();
    int getIntValue();
    List<Integer> getIntList();

    static List<FilterOption> of(FilterType type, List<Integer> options) {
        if (type.getQuery() == QueryType.EQUAL)
            return FilterOptionEnum.of(type, options);

        if (type.getQuery() == QueryType.IN)
            return List.of(new FilterOptionClass(options));

        return List.of(new FilterOptionClass(options.get(0)));
    }
}
