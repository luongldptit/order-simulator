package com.example.order.simulator.model.base;

import com.example.order.simulator.model.enumresponse.DefaultEnum;
import com.example.order.simulator.model.enumresponse.ResponseEnum;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseFactory {

    public <T> ResponseEntity<BaseResponse<T>> success(T data) {
        return response(data, DefaultEnum.SUCCESS, HttpStatus.OK);
    }

    public <T> ResponseEntity<BaseResponse<T>> badRequest(T data, ResponseEnum responseEnum) {
        return response(data, responseEnum, HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<BaseResponse<T>> badRequest(T data, ResponseEnum responseEnum, String... params) {
        return response(data, responseEnum, HttpStatus.BAD_REQUEST, params);
    }

    public <T> ResponseEntity<BaseResponse<T>> internalServerError(T data, ResponseEnum responseEnum) {
        return response(data, responseEnum, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public <T> ResponseEntity<BaseResponse<T>> response(T data, ResponseEnum responseEnum, HttpStatusCode status) {
        return ResponseEntity.status(status).body(new BaseResponse<>(responseEnum.getStatus(), responseEnum.getMessage(), data));
    }

    public <T> ResponseEntity<BaseResponse<T>> response(T data, ResponseEnum responseEnum, HttpStatusCode status, String... params) {
        if (params.length == 0) {
            return ResponseEntity.status(status).body(new BaseResponse<>(responseEnum.getStatus(), responseEnum.getMessage(), data));
        }
        return ResponseEntity.status(status).body(new BaseResponse<>(responseEnum.getStatus(), String.format(responseEnum.getMessage(), params), data));
    }

}
