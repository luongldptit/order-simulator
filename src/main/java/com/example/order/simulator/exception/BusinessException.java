package com.example.order.simulator.exception;

import com.example.order.simulator.model.enumresponse.ResponseEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ResponseEnum responseEnum;
    private final Object data;
    private final String[] parameters;

    public BusinessException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
        this.data = null;
        this.parameters = new String[]{};
    }

    public BusinessException(ResponseEnum responseEnum, Object data) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
        this.data = data;
        this.parameters = new String[]{};
    }

    public BusinessException(ResponseEnum responseEnum, Object data, String... parameters) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
        this.data = data;
        this.parameters = parameters;
    }
}
