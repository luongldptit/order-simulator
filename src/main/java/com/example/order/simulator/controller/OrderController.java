package com.example.order.simulator.controller;

import com.example.order.simulator.model.base.BaseResponse;
import com.example.order.simulator.model.base.ResponseFactory;
import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.request.CreateOrderRequest;
import com.example.order.simulator.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/simulator/order")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @Parameter(name = "Authorization", description = "Basic token", required = true, in = ParameterIn.HEADER)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<BaseResponse<Order>> createOrder(
            @Valid @RequestBody
            CreateOrderRequest request) {
        Order created = orderService.createOrder(request);
        return ResponseFactory.success(created);
    }

    @Operation(
            summary = "Get all orders",
            description = "Returns a list of all orders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of orders",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Parameter(name = "Authorization", description = "Basic token", required = true, in = ParameterIn.HEADER)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<BaseResponse<List<Order>>> listOrders() {
        return ResponseFactory.success(orderService.getAllOrders());
    }

    @Operation(
            summary = "Get order by ID",
            description = "Returns the order details for the given order ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @Parameter(name = "Authorization", description = "Basic token", required = true, in = ParameterIn.HEADER)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<BaseResponse<Order>> getOrder(
            @Parameter(description = "ID of the order to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseFactory.success(orderService.getOrderById(id));
    }

    @Operation(
            summary = "Cancel an order",
            description = "Cancels the order with the given ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @Parameter(name = "Authorization", description = "Basic token", required = true, in = ParameterIn.HEADER)
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<BaseResponse<Order>> cancelOrder(
            @Parameter(description = "ID of the order to cancel", required = true)
            @PathVariable Long id) {
        Order cancelled = orderService.cancelOrder(id);
        return ResponseFactory.success(cancelled);
    }

    @Operation(
            summary = "Simulate order execution",
            description = "Triggers the simulation of order execution. This will update the status of orders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Simulation triggered",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Parameter(name = "Authorization", description = "Basic token", required = true, in = ParameterIn.HEADER)
    @PostMapping("/simulate-execution")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> simulateExecution() {
        orderService.simulateExecution();
        return ResponseFactory.success("Simulation triggered. Please check orders status.");
    }
}
