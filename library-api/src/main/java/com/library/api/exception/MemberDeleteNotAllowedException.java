package com.library.api.exception;

public class MemberDeleteNotAllowedException extends RuntimeException {
    public MemberDeleteNotAllowedException(String message) {
        super(message);
    }
}
