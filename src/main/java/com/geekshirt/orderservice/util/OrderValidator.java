package com.geekshirt.orderservice.util;

import com.geekshirt.orderservice.DTO.OrderRequest;
import com.geekshirt.orderservice.exception.IncorrectOrderRequestException;


public class OrderValidator {
    public static boolean validateOrder(OrderRequest orderRequest){
        if(orderRequest.getItems() == null || orderRequest.getItems().isEmpty()){
            throw new IncorrectOrderRequestException(ExceptionMessagesEnum.INCORRECT_REQUEST_EMPTY_ITEMS_ORDER.getValue());
        }
        return true;
    }
}
