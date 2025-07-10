package com.example.order.simulator.model.enumresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEnum implements ResponseEnum {
    ORDER_NOT_FOUND("ORDER_0001", "Order %s not found"),
    ORDER_ALREADY_CANCELLED("ORDER_0002", "Cannot cancel order %s as it is %s"),
    ;
    private final String status;
    private final String message;
}
