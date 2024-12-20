package com.fs19.webservice.exceptions;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
