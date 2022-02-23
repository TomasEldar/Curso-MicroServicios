package com.geekshirt.orderservice.service;

import com.geekshirt.orderservice.DTO.AccountDto;
import com.geekshirt.orderservice.DTO.OrderRequest;
import com.geekshirt.orderservice.client.CustomerServiceClient;
import com.geekshirt.orderservice.dao.JpaOrderDAO;
import com.geekshirt.orderservice.entities.Order;
import com.geekshirt.orderservice.entities.OrderDetail;
import com.geekshirt.orderservice.exception.AccountNotFoundException;
import com.geekshirt.orderservice.exception.OrderNotFoundException;
import com.geekshirt.orderservice.util.Constants;
import com.geekshirt.orderservice.util.ExceptionMessagesEnum;
import com.geekshirt.orderservice.util.OrderStatus;
import com.geekshirt.orderservice.util.OrderValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private JpaOrderDAO jpaOrderDAO;

    @Transactional
    public Order createOrder(OrderRequest orderRequest){
        OrderValidator.validateOrder(orderRequest);
        AccountDto account = customerServiceClient.findAccountById(orderRequest.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessagesEnum.ACCOUNT_NOT_FOUND.getValue()));
        Order newOrder = initOrder(orderRequest);
        return jpaOrderDAO.save(newOrder);
    }

    private Order initOrder(OrderRequest orderRequest){
        Order orderobj = new Order();
        orderobj.setOrderId(UUID.randomUUID().toString());
        orderobj.setAccountId(orderRequest.getAccountId());
        orderobj.setStatus(OrderStatus.PENDING);

        List<OrderDetail> orderDetails = orderRequest.getItems().stream().map(item -> OrderDetail.builder()
                .price(item.getPrice())
                .quantity(item.getQuantity()).upc(item.getUpc())
                .tax(item.getQuantity() * item.getPrice() * Constants.TAX_IMPORT)
                .order(orderobj).build()).collect(Collectors.toList());

        orderobj.setDetails(orderDetails);
        orderobj.setTotalAmount(orderDetails.stream().mapToDouble(OrderDetail::getPrice).sum());
        orderobj.setTotalTax(orderobj.getTotalAmount() * Constants.TAX_IMPORT);
        orderobj.setTransactionDate(new Date());
        return orderobj;
    }

    public List<Order> findAllOrders(){
        return jpaOrderDAO.findAll();
    }

    public Order findOrderById(String orderID){
        return jpaOrderDAO.findByOrderId(orderID).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
    }

    public Order findById(Long id){
        return jpaOrderDAO.findById(id).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
    }
}
