package com.capstone.shop.user.v1.search;

import com.capstone.shop.core.domain.enums.MerchandiseQualityState;
import com.capstone.shop.core.domain.enums.TransactionMethod;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FilterOptionEnum implements FilterOption {
    NEGO0(FilterType.NEGO, 0, true),
    NEGO1(FilterType.NEGO, 1, false),
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

    public static List<FilterOption> of(FilterType filterType, List<Integer> options) {
        if (filterType == FilterType.TRAN &&
                options.size() == 2 &&
                options.get(0) == 0 &&
                options.get(1) == 1) {
            return List.of(TRAN2);
        }

        return Arrays.stream(FilterOptionEnum.values())
                .filter(o -> o.filterType == filterType)
                .filter(o -> options.contains(o.option))
                .map(o -> (FilterOption) o)
                .toList();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int getIntValue() {
        return (int) value;
    }

    @Override
    public List<Integer> getIntList() {
        return (List<Integer>) value;
    }
}