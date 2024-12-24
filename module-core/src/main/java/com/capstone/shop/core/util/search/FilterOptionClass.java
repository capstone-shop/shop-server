package com.capstone.shop.core.util.search;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterOptionClass implements FilterOption {

    private Object value;

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
