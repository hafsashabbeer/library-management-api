package com.library.api.exception;

public class BookDeleteNotAllowedException extends RuntimeException {
    public BookDeleteNotAllowedException(String message) {
        super(message);
    }
}
