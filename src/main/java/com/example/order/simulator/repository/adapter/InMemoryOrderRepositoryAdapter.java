package com.example.order.simulator.repository.adapter;

import com.example.order.simulator.mapper.OrderMapper;
import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.dto.OrderStatus;
import com.example.order.simulator.repository.memdatabase.repository.OrderRepository;
import com.example.order.simulator.repository.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter for in-memory order repository.
 * This class implements the OrderRepositoryPort interface and provides methods to interact with the in-memory order repository.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(name = "app.repository.in-memory", havingValue = true, matchIfMissing = true)
public class InMemoryOrderRepositoryAdapter implements OrderRepositoryPort {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        if (order == null) {
            return null;
        }
        return orderMapper.toDto(orderRepository.save(orderMapper.toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return orderRepository.delete(id);
    }

    @Override
    public List<Order> findAll() {
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public List<Order> getAllOrdersByStatus(OrderStatus status) {
        if (status == null) {
            return List.of();
        }
        return orderMapper.toDtoList(orderRepository.getAllOrdersByStatus(status));
    }
}
