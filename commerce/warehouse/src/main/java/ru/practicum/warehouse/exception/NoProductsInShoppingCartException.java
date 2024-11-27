package ru.practicum.warehouse.exception;

public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}
