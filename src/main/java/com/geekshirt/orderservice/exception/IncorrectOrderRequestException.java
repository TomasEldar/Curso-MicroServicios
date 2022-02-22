package com.geekshirt.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Empty Items Not Allowed in Order.")
public class IncorrectOrderRequestException extends RuntimeException{

    public IncorrectOrderRequestException(String message){
        super(message);
    }
}
