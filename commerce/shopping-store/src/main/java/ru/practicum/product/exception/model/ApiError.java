package ru.practicum.product.exception.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private String httpStatus;
    private String userMessage;
    private String message;
    private Throwable[] suppressed;
    private String localizedMessage;
}
