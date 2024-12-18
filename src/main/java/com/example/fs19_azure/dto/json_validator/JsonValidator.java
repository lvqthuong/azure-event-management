package com.example.fs19_azure.dto.json_validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class JsonValidator implements ConstraintValidator<ValidJson, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Let @NotBlank handle null/empty validation
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(value); // Parse as JSON
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
