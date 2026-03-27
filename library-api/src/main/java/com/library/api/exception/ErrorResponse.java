package com.library.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class ErrorResponse {

    private LocalDateTime timeStamp;
    private int status;
    private String message;
    private String path;

    public ErrorResponse(LocalDateTime timeStamp, int status, String message, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

}
