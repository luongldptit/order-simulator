package com.example.order.simulator.service.impl;

import com.example.order.simulator.exception.BusinessException;
import com.example.order.simulator.mapper.OrderMapper;
import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.dto.OrderStatus;
import com.example.order.simulator.model.enumresponse.OrderEnum;
import com.example.order.simulator.model.request.CreateOrderRequest;
import com.example.order.simulator.repository.port.OrderRepositoryPort;
import com.example.order.simulator.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepositoryPort orderRepositoryPort;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        // Tạo đối tượng Order mới từ dữ liệu request
        Order order = orderMapper.createOrderRequestToOrder(request);
        order = orderRepositoryPort.save(order);
        log.info("Created order: id={}, symbol={}, side={}, qty={}, price={}",
                order.getId(), order.getSymbol(), order.getSide(), order.getQuantity(), order.getPrice());
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Order getOrderById(Long id) {
        return orderRepositoryPort.findById(id)
                .orElseThrow(() -> new BusinessException(OrderEnum.ORDER_NOT_FOUND, id.toString()));
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        if (OrderStatus.PENDING.equals(order.getStatus())) {
            throw new BusinessException(OrderEnum.ORDER_ALREADY_CANCELLED, id.toString(), order.getStatus().name());
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepositoryPort.save(order);
        log.info("Cancelled order id={} thành công.", id);
        return order;
    }

    @Override
    @Transactional
    public void simulateExecution() {
        Random rand = new Random();
        orderRepositoryPort
                .getAllOrdersByStatus(OrderStatus.PENDING).
                forEach(order -> {
                    if (rand.nextBoolean()) {
                        order.setStatus(OrderStatus.EXECUTED);
                        orderRepositoryPort.save(order);
                        log.info("Simulated execution: Order id={} executed.", order.getId());
                    }
                });
    }

}
