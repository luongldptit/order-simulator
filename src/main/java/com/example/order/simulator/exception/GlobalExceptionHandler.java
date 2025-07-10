package com.example.order.simulator.exception;

import com.example.order.simulator.model.base.BaseResponse;
import com.example.order.simulator.model.base.ResponseFactory;
import com.example.order.simulator.model.enumresponse.DefaultEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
@Hidden
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @SneakyThrows
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(
                        Collectors.groupingBy(FieldError::getField,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "))))
                );
        log.error("Validation error: {}", objectMapper.writeValueAsString(errors), ex);
        return ResponseFactory.badRequest(errors, DefaultEnum.VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Object>> handleBusinessExceptions(
            BusinessException ex) {
        log.error("Business Exception occurred: {}", ex.getMessage(), ex);
        return ResponseFactory.badRequest(ex.getData(), ex.getResponseEnum(), ex.getParameters());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleCommonException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        return ResponseFactory.internalServerError(null, DefaultEnum.SERVER_ERROR);
    }
}
