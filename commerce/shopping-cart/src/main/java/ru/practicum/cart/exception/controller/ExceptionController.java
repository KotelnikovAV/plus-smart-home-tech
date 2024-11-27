package ru.practicum.cart.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.cart.exception.DataDuplicationException;
import ru.practicum.cart.exception.NoProductsInShoppingCartException;
import ru.practicum.cart.exception.NotFoundException;
import ru.practicum.cart.exception.model.ApiError;

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
    public ApiError handleNotFoundException(NotFoundException e) {
        log.error("404 {} ", e.getMessage());
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
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleNotAuthorizedUserException(MissingServletRequestParameterException e) {
        log.error("401 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.UNAUTHORIZED.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("User is not logged in")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataDuplicationException(DataDuplicationException e) {
        log.error("400 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("This user already has an active shopping cart")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        log.error("400 {} ", e.getMessage());
        return ApiError.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .localizedMessage(e.getLocalizedMessage())
                .suppressed(e.getSuppressed())
                .userMessage("There are no desired items in the shopping cart")
                .message(e.getMessage())
                .build();
    }
}
