package com.example.order.simulator.repository.memdatabase.entity;

import com.example.order.simulator.model.dto.OrderStatus;
import com.example.order.simulator.model.dto.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderEntity {
    private Long id;
    private String symbol;
    private BigDecimal price;
    private int quantity;
    private Side side;
    private OrderStatus status;
    private LocalDateTime createdTime;
}
