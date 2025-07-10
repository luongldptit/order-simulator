package com.example.order.simulator.model.request;

import com.example.order.simulator.model.dto.OrderStatus;
import com.example.order.simulator.model.dto.Side;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Request object for creating a new order")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateOrderRequest {
    @Schema(description = "Stock symbol", example = "AAPL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @NotBlank
    private String symbol;

    @Schema(description = "Order price, must be greater than 0", example = "150.50", requiredMode = Schema.RequiredMode.REQUIRED)
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal price;

    @Schema(description = "Order quantity, must be non-negative", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 0)
    private int quantity;

    @Schema(description = "Order side (BUY or SELL)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Side side;

    @Schema(description = "Order status", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private OrderStatus status;
}
