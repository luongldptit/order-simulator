package com.example.order.simulator.repository.port;

import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.dto.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);

    Optional<Order> findById(Long id);

    boolean deleteById(Long id);

    List<Order> findAll();

    List<Order> getAllOrdersByStatus(OrderStatus status);
}
