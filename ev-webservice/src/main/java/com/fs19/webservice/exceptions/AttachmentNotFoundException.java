package com.example.fs19_azure.exceptions;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
