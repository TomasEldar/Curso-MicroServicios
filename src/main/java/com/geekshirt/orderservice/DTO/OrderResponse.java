package com.geekshirt.orderservice.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class OrderResponse {

    private String orderId;
    private String status;
    private String accountId;
    private Double totalAmount;
    private Double totalTax;
    private Date transactionDate;

}
