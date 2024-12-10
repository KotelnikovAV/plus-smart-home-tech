package ru.practicum.delivery.exception;

public class NotExistentWarehouseAddressException extends RuntimeException {
    public NotExistentWarehouseAddressException(String message) {
        super(message);
    }
}
