package com.example.fs19_azure.exceptions;

public class SendMessageProcessingException extends RuntimeException {

    public SendMessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
