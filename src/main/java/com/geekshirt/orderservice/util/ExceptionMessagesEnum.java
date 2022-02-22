package com.geekshirt.orderservice.util;

import lombok.Getter;

@Getter
public enum ExceptionMessagesEnum {
    ACCOUNT_NOT_FOUND("Account Not Found"),
    INCORRECT_REQUEST_EMPTY_ITEMS_ORDER("Empty items are not allowed in the order.");

    ExceptionMessagesEnum(String message){
        value = message;
    }

    private final String value;
}
