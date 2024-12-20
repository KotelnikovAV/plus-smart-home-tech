package ru.practicum.payment.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.payment.exception.NoPaymentFoundException;
import ru.practicum.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.payment.exception.model.ApiError;

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
    public ApiError handleNoPaymentFoundException(NoPaymentFoundException e) {
        log.error("404 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.NOT_FOUND.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("There is no such payment")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotEnoughInfoInOrderToCalculateException(NotEnoughInfoInOrderToCalculateException e) {
        log.error("400 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("There is not enough information to calculate the cost")
                .message(e.getMessage())
                .build();
    }
}
