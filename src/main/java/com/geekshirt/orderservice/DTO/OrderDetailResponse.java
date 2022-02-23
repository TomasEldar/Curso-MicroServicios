package com.geekshirt.orderservice.DTO;

import lombok.Data;

@Data
public class OrderDetailResponse {

    private long id;
    private int quantity;
    private double price;
    private double tax;
    private String upc;
}
