package com.jfouad.provider;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.jfouad.model.Mail;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.MessageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.mailjet.client.transactional.response.SentMessageStatus.SUCCESS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class MailJetSendMail implements SendMail {

    final private LambdaLogger logger = LambdaRuntime.getLogger();

    final private ClientOptions options;

    final private MailjetClient client;

    final private String contactSupportMail;

    public MailJetSendMail(String publicKey, String privateKey, String contactSupportMail) {
        this.options = ClientOptions.builder()
                .apiKey(publicKey)
                .apiSecretKey(privateKey)
                .build();

        this.client = new MailjetClient(options);;

        this.contactSupportMail = contactSupportMail;
    }

    @Override
    public boolean send(Mail mail) {

        final TransactionalEmail message = TransactionalEmail
                .builder()
                .to(getRecipientsEmail(mail.getTo()))
                .from(getSenderEmail(mail.getFrom()))
                .htmlPart(mail.getBody())
                .subject(mail.getFrom())
                .build();

        try {
            MessageResult[] responses = SendEmailsRequest
                    .builder()
                    .message(message)
                    .build()
                    .sendWith(client)
                    .getMessages();

            return Arrays.stream(responses)
                    .map(MessageResult::getStatus)
                    .filter(Objects::nonNull)
                    .allMatch(SUCCESS::equals);

        } catch (MailjetException e) {
            logger.log("An error occurred during jetmail " + e.getMessage());
            return false;
        }
    }

    SendContact getSenderEmail(String senderMail) {
        return new SendContact(ofNullable(senderMail).orElse(contactSupportMail));
    }

    List<SendContact> getRecipientsEmail(List<String> recipients) {
        return ofNullable(recipients)
                .orElse(emptyList())
                .stream()
                .map(SendContact::new)
                .collect(toList());
    }
}
