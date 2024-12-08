package ru.practicum.delivery.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.delivery.exception.DuplicateDeliveryException;
import ru.practicum.delivery.exception.NoDeliveryFoundException;
import ru.practicum.delivery.exception.NotExistentWarehouseAddressException;
import ru.practicum.delivery.exception.model.ApiError;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleCommonException(Exception e) {
        log.error("500 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("INTERNAL_SERVER_ERROR")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoDeliveryFoundException(NoDeliveryFoundException e) {
        log.error("404 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.NOT_FOUND.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("There is no such delivery")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotExistentWarehouseAddressException(NotExistentWarehouseAddressException e) {
        log.error("400 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("Non-existent warehouse address")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateDeliveryException(DuplicateDeliveryException e) {
        log.error("409 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.CONFLICT.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("Duplicate delivery")
                .message(e.getMessage())
                .build();
    }
}
