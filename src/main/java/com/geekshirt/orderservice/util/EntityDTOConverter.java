package com.geekshirt.orderservice.util;

import com.geekshirt.orderservice.DTO.OrderResponse;
import com.geekshirt.orderservice.entities.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public OrderResponse convertEntityToDTO(Order order){
        return modelMapper.map(order,OrderResponse.class);
    }

    public List<OrderResponse> convertEntityToDTO(List<Order> orders){
        return orders.stream().map(order -> convertEntityToDTO(order)).collect(Collectors.toList());
    }
}
