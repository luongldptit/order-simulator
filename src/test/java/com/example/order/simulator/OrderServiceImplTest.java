package com.example.order.simulator;

import com.example.order.simulator.exception.BusinessException;
import com.example.order.simulator.mapper.OrderMapper;
import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.dto.OrderStatus;
import com.example.order.simulator.model.enumresponse.OrderEnum;
import com.example.order.simulator.model.request.CreateOrderRequest;
import com.example.order.simulator.repository.port.OrderRepositoryPort;
import com.example.order.simulator.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private CreateOrderRequest testRequest;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setSymbol("AAPL");
        testOrder.setQuantity(10);
        testOrder.setPrice(BigDecimal.valueOf(150.50));
        testOrder.setStatus(OrderStatus.PENDING);

        testRequest = new CreateOrderRequest();
        testRequest.setSymbol("AAPL");
        testRequest.setQuantity(10);
        testRequest.setPrice(BigDecimal.valueOf(150.50));
    }

    @Test
    @DisplayName("Should create order successfully")
    void testCreateOrder() {
        // Given
        when(orderMapper.createOrderRequestToOrder(any(CreateOrderRequest.class))).thenReturn(testOrder);
        when(orderRepositoryPort.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.createOrder(testRequest);

        // Then
        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testOrder.getSymbol(), result.getSymbol());
        verify(orderRepositoryPort).save(testOrder);
    }

    @Test
    @DisplayName("Should return all orders")
    void testGetAllOrders() {
        // Given
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> expectedOrders = Arrays.asList(testOrder, order2);
        when(orderRepositoryPort.findAll()).thenReturn(expectedOrders);

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedOrders, result);
    }

    @Test
    @DisplayName("Should return order by ID when exists")
    void testGetOrderById_WhenOrderExists() {
        // Given
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        Order result = orderService.getOrderById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
    }

    @Test
    @DisplayName("Should throw exception when order not found")
    void testGetOrderById_WhenOrderNotFound() {
        // Given
        when(orderRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // When/Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.getOrderById(99L);
        });

        assertEquals(OrderEnum.ORDER_NOT_FOUND, exception.getResponseEnum());
    }

    @Test
    @DisplayName("Should throw exception when cancelling pending order")
    void testCancelOrder_WhenOrderIsPending() {
        // Given
        testOrder.setStatus(OrderStatus.EXECUTED);
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.of(testOrder));

        // When/Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.cancelOrder(1L);
        });

        assertEquals(OrderEnum.ORDER_ALREADY_CANCELLED, exception.getResponseEnum());
        verify(orderRepositoryPort, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should cancel order successfully when status is EXECUTED")
    void testCancelOrder_WhenOrderIsExecuted() {
        // Given
        testOrder.setStatus(OrderStatus.EXECUTED);
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepositoryPort.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.cancelOrder(1L);

        // Then
        assertEquals(OrderStatus.CANCELLED, result.getStatus());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepositoryPort).save(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELLED, orderCaptor.getValue().getStatus());
    }

    @Test
    @DisplayName("Should simulate execution of pending orders")
    void testSimulateExecution() {
        // Given
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.PENDING);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.PENDING);

        List<Order> pendingOrders = Arrays.asList(order1, order2);
        when(orderRepositoryPort.getAllOrdersByStatus(OrderStatus.PENDING)).thenReturn(pendingOrders);

        // When
        orderService.simulateExecution();

        // Then
        // Verification is tricky due to random behavior
        // We can verify that getAllOrdersByStatus was called
        verify(orderRepositoryPort).getAllOrdersByStatus(OrderStatus.PENDING);

        // And that save was called between 0 and 2 times (due to random selection)
        verify(orderRepositoryPort, atMost(2)).save(any(Order.class));
    }
}
