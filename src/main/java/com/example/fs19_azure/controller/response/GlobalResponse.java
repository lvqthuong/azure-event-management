package com.example.fs19_azure.controller.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GlobalResponse<T> {
    public final static String SUCCESS = "success";
    public final static String ERROR = "error";

    private final String status;
    private final int code;
    public final T data;
    private final List<ErrorItem> errors;


    public GlobalResponse(int code, List<ErrorItem> errors) {
        this.status = ERROR;
        this.code = code;
        this.data = null;
        this.errors = errors;
    }

    public GlobalResponse(int code, T data) {
        this.status = SUCCESS;
        this.code = code;
        this.data = data;
        this.errors = null;
    }

    @Getter
    public static class ErrorItem {
        private final String message;

        public ErrorItem(String message) {
            this.message = message;
        }
    }
}
