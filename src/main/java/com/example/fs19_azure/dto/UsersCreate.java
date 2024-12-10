package com.example.fs19_azure.dto;

import jakarta.validation.constraints.NotBlank;

public record UsersCreate(

    @NotBlank(message = "must not be empty or whitespace")
    String email,

    @NotBlank(message = "must not be empty or whitespace")
    String password,

    @NotBlank(message = "must not be empty or whitespace")
    String name
) {
}
