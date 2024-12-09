package com.capstone.shop.user.v1.search;

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
}
