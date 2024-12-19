package com.fs19.webservice.dto;

import com.example.fs19_azure.dto.json_validator.ValidJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = false)
public record EventsCreate(
        @NotBlank(message = "must not be empty or whitespace")
        String name,

        @NotBlank(message = "must not be empty or whitespace")
        String type,

        String description,

        @NotBlank(message = "must not be empty or whitespace")
        String location,

        @NotBlank(message = "must not be empty or whitespace")
        String startDate,

        @NotBlank(message = "must not be empty or whitespace")
        String endDate,

        @NotBlank(message = "must not be empty or whitespace")
        String organizerId,

        @ValidJson
        String metadata
) {
}
