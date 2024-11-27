package ru.practicum.cart.exception;

public class DataDuplicationException extends RuntimeException {
    public DataDuplicationException(String message) {
        super(message);
    }
}
