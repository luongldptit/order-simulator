package com.example.order.simulator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Order {
    @Schema(description = "Order ID", example = "123")
    private Long id;

    @Schema(description = "Stock symbol", example = "AAPL")
    private String symbol;

    @Schema(description = "Order price", example = "150.50")
    private BigDecimal price;

    @Schema(description = "Order quantity", example = "100")
    private int quantity;

    @Schema(description = "Order side (BUY or SELL)")
    private Side side;

    @Schema(description = "Order status")
    private OrderStatus status;

    @Schema(description = "Order creation time")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdTime;
}
