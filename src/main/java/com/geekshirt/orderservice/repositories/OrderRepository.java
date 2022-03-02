package com.geekshirt.orderservice.repositories;

import com.geekshirt.orderservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findOrdersByAccountId(String accoundId);

    public Order findOrdersByOrderId(String orderId);
}
