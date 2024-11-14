package com.capstone.shop.user.v1.util;

import com.capstone.shop.entity.Merchandise;
import com.capstone.shop.enums.MerchandiseQualityState;
import com.capstone.shop.enums.TransactionMethod;
import jakarta.persistence.criteria.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Filter {
    @Getter
    @AllArgsConstructor
    public enum QueryType {
        EQUAL,
        GREATER_OR_EQUAL
    }

    @Getter
    @AllArgsConstructor
    public enum FilterType {
        NEGO("nego", "negotiationAvailable", QueryType.EQUAL),
        WISH("wish", "wish", QueryType.GREATER_OR_EQUAL),
        REPU("repu", "register.reputation", QueryType.GREATER_OR_EQUAL),
        TRAN("tran", "transactionMethod", QueryType.EQUAL),
        STAT("stat", "merchandiseState", QueryType.EQUAL);
        private final String filterName, columnName;
        private final QueryType query;
        public static FilterType findByFilterName(String filterName) {
            for (FilterType filterType : FilterType.values()) {
                if (filterType.filterName.equals(filterName)) {
                    return filterType;
                }
            }
            return null;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum FilterOption {
        NEGO0(FilterType.NEGO, 0, true),
        NEGO1(FilterType.NEGO, 1, false),
        WISH0(FilterType.WISH, 0, 5),
        WISH1(FilterType.WISH, 1, 10),
        WISH2(FilterType.WISH, 2, 20),
        REPU0(FilterType.REPU, 0, 50),
        REPU1(FilterType.REPU, 1, 75),
        REPU2(FilterType.REPU, 2, 90),
        TRAN0(FilterType.TRAN, 0, TransactionMethod.DIRECT),
        TRAN1(FilterType.TRAN, 1, TransactionMethod.DELIVERY),
        TRAN2(FilterType.TRAN, 2, TransactionMethod.BOTH),
        STAT0(FilterType.STAT, 0, MerchandiseQualityState.NEW),
        STAT1(FilterType.STAT, 1, MerchandiseQualityState.GOOD),
        STAT2(FilterType.STAT, 2, MerchandiseQualityState.AVERAGE),
        STAT3(FilterType.STAT, 3, MerchandiseQualityState.BAD),
        STAT4(FilterType.STAT, 4, MerchandiseQualityState.BROKEN);
        private final FilterType filterType;
        private final int option;
        private final Object value;
        public int getIntValue() {
            return (int) value;
        }
        public static List<FilterOption> findBy(FilterType filterType, List<Integer> options) {
            if (filterType == FilterType.TRAN &&
                    options.size() == 2 &&
                    options.get(0) == 0 &&
                    options.get(1) == 1) {
                return List.of(TRAN2);
            }

            var result = new ArrayList<FilterOption>();
            for (FilterOption filterOption : FilterOption.values()) {
                if (filterOption.filterType == filterType &&
                        options.contains(filterOption.option)) {
                    result.add(filterOption);
                }
            }
            return result;
        }
    }

    private final Map<FilterType, List<FilterOption>> typeToOptionMap = new HashMap<>();

    public Filter(String filterString) {
        String[] filters = filterString.split(";");
        for (String filter : filters) {
            String[] filterAndOption = filter.split(",");
            FilterType filterType = FilterType.findByFilterName(filterAndOption[0]);
            List<Integer> options = Arrays.stream(filterAndOption).
                    skip(1).
                    map(Integer::parseInt).
                    sorted().
                    toList();
            typeToOptionMap.put(filterType, FilterOption.findBy(filterType, options));
        }
    }

    public List<FilterOption> getFilterOptions(FilterType filterType) {
        return typeToOptionMap.get(filterType);
    }
}
