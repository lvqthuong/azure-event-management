package com.example.fs19_azure.dto;

import jakarta.validation.constraints.NotBlank;

public record EventsUpdate(
    @NotBlank(message = "must not be empty or whitespace")
    String name,

    String description,

    @NotBlank(message = "must not be empty or whitespace")
    String location,

    @NotBlank(message = "must not be empty or whitespace")
    String startDate,

    @NotBlank(message = "must not be empty or whitespace")
    String endDate,

    @NotBlank(message = "must not be empty or whitespace")
    String organizerId,

    String metadata
) {
}