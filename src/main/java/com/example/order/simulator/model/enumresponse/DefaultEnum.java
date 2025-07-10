package com.example.order.simulator.model.enumresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DefaultEnum implements ResponseEnum {
    SUCCESS("0000", "Success"),
    SERVER_ERROR("9999", "Server error"),
    VALIDATION_ERROR("9998", "Validation error"),
    ;
    private final String status;
    private final String message;
}
