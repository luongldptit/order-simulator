package com.example.order.simulator.repository.memdatabase.repository;

import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.dto.OrderStatus;
import com.example.order.simulator.repository.memdatabase.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class OrderRepository {
    private final ConcurrentHashMap<Long, OrderEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public OrderEntity save(OrderEntity order) {
        if (order.getId() == null) {
            // Tạo id mới cho order
            long newId = idGenerator.incrementAndGet();
            order.setId(newId);
            order.setCreatedTime(LocalDateTime.now());
        }
        // put vào map (sẽ replace nếu trùng id có sẵn)
        storage.put(order.getId(), order);
        return order;
    }

    public List<OrderEntity> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<OrderEntity> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    public List<OrderEntity> getAllOrdersByStatus(OrderStatus status) {
        return storage.values().stream().filter(order -> order.getStatus().equals(status)).toList();
    }
}
