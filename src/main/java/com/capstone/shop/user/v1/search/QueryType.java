package com.capstone.shop.user.v1.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueryType {
    EQUAL,
    GREATER_OR_EQUAL,
    IN
}