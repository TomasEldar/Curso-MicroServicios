package com.geekshirt.orderservice.service;

import com.geekshirt.orderservice.DTO.OrderRequest;
import com.geekshirt.orderservice.DTO.OrderResponse;
import com.geekshirt.orderservice.entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    public Order createOrder(OrderRequest orderRequest){
        Order response = new Order();
        response.setAccountId(orderRequest.getAccountId());
        response.setOrderId("9999");
        response.setStatus("PENDING");
        response.setTotalAmount(100.00);
        response.setTotalTax(10.00);
        response.setTransactionDate(new Date());

        return response;
    }

    public List<Order> findAllOrders(){
        List<Order> orderList = new ArrayList<>();

        Order response = new Order();
        response.setAccountId("995215");
        response.setOrderId("9999");
        response.setStatus("PENDING");
        response.setTotalAmount(100.00);
        response.setTotalTax(10.00);
        response.setTransactionDate(new Date());

        Order response2 = new Order();
        response2.setAccountId("995225");
        response2.setOrderId("9998");
        response2.setStatus("PENDING");
        response2.setTotalAmount(120.00);
        response2.setTotalTax(12.00);
        response2.setTransactionDate(new Date());

        orderList.add(response);
        orderList.add(response2);

        return orderList;
    }

    public Order findOrderById(String orderID){
        Order response = new Order();
        response.setAccountId("9999112");
        response.setOrderId(orderID);
        response.setStatus("PENDING");
        response.setTotalAmount(100.00);
        response.setTotalTax(10.00);
        response.setTransactionDate(new Date());

        return response;
    }
}
