package com.example.fs19_azure.dto.json_validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JsonValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidJson {
    String message() default "is not in valid JSON format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
