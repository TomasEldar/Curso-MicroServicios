package com.geekshirt.orderservice.DTO;

import com.geekshirt.orderservice.entities.OrderDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    private String orderId;
    private String status;
    private String accountId;
    private Double totalAmount;
    private Double totalTax;
    private Date transactionDate;
    List<OrderDetailResponse> details;

}
