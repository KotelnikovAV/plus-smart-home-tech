package ru.practicum.order.exception;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}