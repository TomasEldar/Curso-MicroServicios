package com.geekshirt.orderservice.controllers;

import com.geekshirt.orderservice.DTO.OrderRequest;
import com.geekshirt.orderservice.DTO.OrderResponse;
import com.geekshirt.orderservice.entities.Order;
import com.geekshirt.orderservice.exception.PaymentNotAcceptedException;
import com.geekshirt.orderservice.service.OrderService;
import com.geekshirt.orderservice.util.EntityDTOConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityDTOConverter entityDTOConverter;

    @ApiOperation(value = "Retrieve all existed orders",notes = "This operation returns all stored orders")
    @GetMapping(value = "order")
    public ResponseEntity<List<OrderResponse>> findAll(){
        List<Order> orders = orderService.findAllOrders();
        return new ResponseEntity<>(entityDTOConverter.convertEntityToDTO(orders), HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve an order based on ID",notes = "This operation returns an order by Order ID")
    @GetMapping(value = "order/{orderId}")
    public ResponseEntity<OrderResponse> findById(@PathVariable String orderId){
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(entityDTOConverter.convertEntityToDTO(order), HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve an orders based on ID",notes = "This operation returns an order by DB Order ID")
    @GetMapping(value = "order/generated/{orderId}")
    public ResponseEntity<OrderResponse> findByGeneratedId(@PathVariable Long orderId){
        Order order = orderService.findById(orderId);
        return new ResponseEntity<>(entityDTOConverter.convertEntityToDTO(order), HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve an orders based on Account ID",notes = "This operation returns orders by Account ID")
    @GetMapping(value = "order/account/{accountId}")
    public ResponseEntity<List<OrderResponse>> findOrdersByAccountId(@PathVariable String accountId){
        List<Order> orders = orderService.findOrdersByAccountId(accountId);
        return new ResponseEntity<>(entityDTOConverter.convertEntityToDTO(orders), HttpStatus.OK);
    }

    @ApiOperation(value = "Creates an order",notes = "This operation creates a new order")
    @PostMapping(value = "order/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest payload) throws PaymentNotAcceptedException {
        Order order = orderService.createOrder(payload);
        return new ResponseEntity<>(entityDTOConverter.convertEntityToDTO(order), HttpStatus.CREATED);
    }
}
