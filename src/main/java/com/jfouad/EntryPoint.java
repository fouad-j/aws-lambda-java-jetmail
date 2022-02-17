package com.jfouad;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.Set;
import java.util.stream.Collectors;

public class EntryPoint {

    void main(Mail mail) {

        final Set<ConstraintViolation<Mail>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(mail);

        if (!violations.isEmpty()) {
            final String errors = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";"));
            throw new RuntimeException("An error occurred due to invalid request : " + errors);
        }

    }


}
