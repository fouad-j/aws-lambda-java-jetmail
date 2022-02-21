package com.jfouad.service;

import com.jfouad.model.Mail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ValidationService {

    final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    public List<String> validate(Mail mail) {

        final Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

        return violations.isEmpty()
                ? emptyList()
                : violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(toList());
    }
}
