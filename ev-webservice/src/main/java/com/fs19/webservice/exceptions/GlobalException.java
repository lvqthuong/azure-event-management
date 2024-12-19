package com.fs19.webservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public GlobalException(ErrorMessage from) {
        this.status = HttpStatus.BAD_REQUEST;
        this.message = from.getMessage();
    }

    public GlobalException(HttpStatus status, ErrorMessage from) {
        this.status = status;
        this.message = from.getMessage();
    }

    public GlobalException(String message) {
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }
}