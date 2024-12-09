package ru.practicum.order.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.order.exception.NoOrderFoundException;
import ru.practicum.order.exception.NoSpecifiedProductInWarehouseException;
import ru.practicum.order.exception.NotAuthorizedUserException;
import ru.practicum.order.exception.model.ApiError;

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
    public ApiError handleNoOrderFoundException(NoOrderFoundException e) {
        log.error("404 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.NOT_FOUND.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("There is no such order")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        log.error("401 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.UNAUTHORIZED.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("The user is not logged in")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException e) {
        log.error("404 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.NOT_FOUND.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("These products are out of stock")
                .message(e.getMessage())
                .build();
    }
}
