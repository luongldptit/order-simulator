package com.example.order.simulator.mapper;

import com.example.order.simulator.model.dto.Order;
import com.example.order.simulator.model.request.CreateOrderRequest;
import com.example.order.simulator.repository.memdatabase.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper extends BaseMapper<OrderEntity, Order> {
    Order createOrderRequestToOrder(CreateOrderRequest request);
}
