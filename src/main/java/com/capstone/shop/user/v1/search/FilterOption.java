package com.capstone.shop.user.v1.search;

import java.util.List;

public interface FilterOption {
    Object getValue();
    int getIntValue();

    static List<FilterOption> of(FilterType type, List<Integer> options) {
        if (type.getQuery() == QueryType.EQUAL)
            return FilterOptionEnum.of(type, options);

        FilterOption option = new FilterOptionClass(options.get(0));
        return List.of(option);
    }
}
