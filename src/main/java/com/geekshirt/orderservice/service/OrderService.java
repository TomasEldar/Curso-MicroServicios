package com.geekshirt.orderservice.service;

import com.geekshirt.orderservice.DTO.AccountDto;
import com.geekshirt.orderservice.DTO.OrderRequest;
import com.geekshirt.orderservice.client.CustomerServiceClient;
import com.geekshirt.orderservice.entities.Order;
import com.geekshirt.orderservice.exception.AccountNotFoundException;
import com.geekshirt.orderservice.util.ExceptionMessagesEnum;
import com.geekshirt.orderservice.util.OrderValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private CustomerServiceClient customerServiceClient;

    public Order createOrder(OrderRequest orderRequest){
        OrderValidator.validateOrder(orderRequest);
        AccountDto account = customerServiceClient.findAccountById(orderRequest.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessagesEnum.ACCOUNT_NOT_FOUND.getValue()));

        Order response = new Order();
        response.setAccountId(orderRequest.getAccountId());
        response.setOrderId("9999");
        //response.setStatus("PENDING");
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
        //response.setStatus("PENDING");
        response.setTotalAmount(100.00);
        response.setTotalTax(10.00);
        response.setTransactionDate(new Date());

        Order response2 = new Order();
        response2.setAccountId("995225");
        response2.setOrderId("9998");
        //response2.setStatus("PENDING");
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
        //response.setStatus("PENDING");
        response.setTotalAmount(100.00);
        response.setTotalTax(10.00);
        response.setTransactionDate(new Date());

        return response;
    }
}
