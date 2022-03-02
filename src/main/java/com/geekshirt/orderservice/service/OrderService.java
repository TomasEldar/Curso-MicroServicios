package com.geekshirt.orderservice.service;

import com.geekshirt.orderservice.DTO.AccountDto;
import com.geekshirt.orderservice.DTO.Confirmation;
import com.geekshirt.orderservice.DTO.OrderRequest;
import com.geekshirt.orderservice.DTO.ShipmentOrderResponse;
import com.geekshirt.orderservice.client.CustomerServiceClient;
import com.geekshirt.orderservice.client.InventoryServiceClient;
import com.geekshirt.orderservice.entities.Order;
import com.geekshirt.orderservice.entities.OrderDetail;
import com.geekshirt.orderservice.exception.AccountNotFoundException;
import com.geekshirt.orderservice.exception.OrderNotFoundException;
import com.geekshirt.orderservice.exception.PaymentNotAcceptedException;
import com.geekshirt.orderservice.producer.ShippingOrderProducer;
import com.geekshirt.orderservice.repositories.OrderRepository;
import com.geekshirt.orderservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private PaymentProcessorService paymentService;

    @Autowired
    private InventoryServiceClient inventoryClient;

    @Autowired
    private ShippingOrderProducer shipmentMessageProducer;

    @Autowired
    private OrderMailService mailService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Order createOrder(OrderRequest orderRequest) throws PaymentNotAcceptedException{
        OrderValidator.validateOrder(orderRequest);
        AccountDto account = customerServiceClient.findAccountById(orderRequest.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessagesEnum.ACCOUNT_NOT_FOUND.getValue()));
        Order newOrder = initOrder(orderRequest);
        Confirmation confirmation = paymentService.processPayment(newOrder, account);

        log.info("Payment Confirmation: {}", confirmation);

        String paymentStatus = confirmation.getTransactionStatus();
        newOrder.setPaymentStatus(OrderPaymentStatus.valueOf(paymentStatus));

        if (paymentStatus.equals(OrderPaymentStatus.DENIED.name())) {
            newOrder.setStatus(OrderStatus.NA);
            orderRepository.save(newOrder);
            throw new PaymentNotAcceptedException("The Payment added to your account was not accepted, please verify.");
        }

        log.info("Updating Inventory: {}", orderRequest.getItems());
        inventoryClient.updateInventory(orderRequest.getItems());

        log.info("Sending Request to Shipping Service.");
        shipmentMessageProducer.send(newOrder.getOrderId(), account);

        return orderRepository.save(newOrder);
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
                .totalAmount((item.getPrice() * item.getQuantity()))
                .order(orderobj).build()).collect(Collectors.toList());

        orderobj.setDetails(orderDetails);
        orderobj.setTotalAmount(orderDetails.stream().mapToDouble(OrderDetail::getTotalAmount).sum());
        orderobj.setTotalTax(orderobj.getTotalAmount() * Constants.TAX_IMPORT);
        orderobj.setTotalAmountTax(orderobj.getTotalAmount() + orderobj.getTotalTax());
        orderobj.setTransactionDate(new Date());
        return orderobj;
    }

    public List<Order> findAllOrders(){
        return orderRepository.findAll();
    }

    public Order findOrderById(String orderID){
        Optional<Order> order = Optional.ofNullable(orderRepository.findOrdersByOrderId(orderID));
        return order.orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
    }

    public Order findById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
    }

    public List<Order> findOrdersByAccountId(String accountId){
        Optional<List<Order>> orders = Optional.ofNullable(orderRepository.findOrdersByAccountId(accountId));
        return orders.orElseThrow(() -> new OrderNotFoundException("Orders were Not Found"));
    }

    public void updateShipmentOrder(ShipmentOrderResponse response) {
        try {
            Order order = findOrderById(response.getOrderId());
            order.setStatus(OrderStatus.valueOf(response.getShippingStatus()));
            orderRepository.save(order);
            mailService.sendEmail(order, response);
        }
        catch(OrderNotFoundException orderNotFound) {
            log.info("The Following Order was not found: {} with tracking Id: {}", response.getOrderId(), response.getTrackingId());
        }
        catch(Exception e) {
            log.info("An error occurred sending email: " + e.getMessage());
        }
    }
}
