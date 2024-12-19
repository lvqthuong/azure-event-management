package com.example.fs19_azure.exceptions;

import java.io.IOException;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
