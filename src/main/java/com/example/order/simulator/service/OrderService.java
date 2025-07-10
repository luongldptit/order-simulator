package com.example.order.simulator.service;

import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order cancelOrder(Long id);

    void simulateExecution();
}
