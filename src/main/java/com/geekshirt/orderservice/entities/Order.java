package com.geekshirt.orderservice.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Order {

    private String orderId;
    private String status;
    private String accountId;
    private Double totalAmount;
    private Double totalTax;
    private Date transactionDate;

}
