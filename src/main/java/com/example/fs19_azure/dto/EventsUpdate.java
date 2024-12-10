package com.example.fs19_azure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = false)
public record EventsUpdate(
    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String name,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String type,

    String description,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String location,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String startDate,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String endDate,

    String metadata
) {
}