package com.example.order.simulator.model.base;

import lombok.Getter;

@Getter
public class BaseResponse<T> {

    private final String status;
    private final String message;
    private final T data;

    public BaseResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
