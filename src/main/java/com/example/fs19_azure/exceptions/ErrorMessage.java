package com.example.fs19_azure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    //Users
    USER_NOT_FOUND("User not found"),
    USER_ALREADY_EXISTS("User already exists"),

    //Events
    EVENT_CREATION_FAILED("Event creation failed");


    final String message;
}
