package com.capstone.shop.user.v1.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filter {

    private final Map<FilterType, List<FilterOption>> typeToOptionMap = new HashMap<>();

    public Filter(String filterString) {
        String[] filters = filterString.split(";");
        for (String filter : filters) {
            String[] filterAndOption = filter.split(",");
            FilterType filterType = FilterType.findByFilterName(filterAndOption[0]);
            if (filterType == null)
                continue;

            List<Integer> options = Arrays.stream(filterAndOption)
                    .skip(1)
                    .map(Integer::parseInt)
                    .sorted()
                    .toList();

            typeToOptionMap.put(filterType, FilterOption.of(filterType, options));
        }
    }

    public List<FilterOption> getFilterOptions(FilterType filterType) {
        return typeToOptionMap.get(filterType);
    }
}
