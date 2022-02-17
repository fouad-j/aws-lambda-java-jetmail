package com.jfouad;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.jfouad.model.Mail;
import com.jfouad.provider.MailJetSendMail;
import com.jfouad.provider.SendMail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

public class EntryPoint implements RequestHandler<Mail, Boolean> {

    final SendMail sendMail = new MailJetSendMail();

    final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Override
    public Boolean handleRequest(Mail mail, Context context) {
        context.getLogger().log("Input: " + mail);

        final Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

        if (!violations.isEmpty()) {
            final String errors = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";"));
            throw new RuntimeException("An error occurred due to invalid request : " + errors);
        }

        return sendMail.send(mail);
    }

}
