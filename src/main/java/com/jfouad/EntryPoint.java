package com.jfouad;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.jfouad.model.Mail;
import com.jfouad.provider.MailJetSendMail;
import com.jfouad.provider.SendMail;
import com.jfouad.service.ValidationService;

import java.util.List;

import static java.lang.String.join;

public class EntryPoint implements RequestHandler<Mail, Boolean> {

    final SendMail sendMail = new MailJetSendMail();

    final ValidationService validationService = new ValidationService();

    @Override
    public Boolean handleRequest(Mail mail, Context context) {
        context.getLogger().log("Input: " + mail);

        final List<String> violations = validationService.validate(mail);

        if (!violations.isEmpty()) {
            throw new RuntimeException("An error occurred due to invalid request : " + join(";", violations));
        }

        return sendMail.send(mail);
    }

}
