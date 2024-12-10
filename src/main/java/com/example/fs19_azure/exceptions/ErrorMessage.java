package com.example.fs19_azure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    //Users
    USER_NOT_FOUND("User not found")
    , USER_ALREADY_EXISTS("User already exists")
    , EMAIL_ALREADY_USED("Email already used")



    //Events
    , EVENT_CREATION_FAILED("Event creation failed")
    , EVENT_NOT_FOUND("Event not found")
    , EVENT_UPDATE_FAILED("Event update failed");


    final String message;
}
