package com.jfouad;

import com.jfouad.provider.MailJetSendMail;
import com.mailjet.client.transactional.SendContact;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class MailJetSendMailTest {

    MailJetSendMail sendMail = new MailJetSendMail();

    @Test
    void should_return_send_contact_object() {
        // GIVEN
        final String mockedMail = "test@gmail.com";

        // WHEN
        final SendContact senderEmail = sendMail.getSenderEmail(mockedMail);

        // THEN
        assertThat(senderEmail).isEqualTo(new SendContact("test@gmail.com"));
    }


    @Test
    void should_return_list_of_send_contact_object() {
        // GIVEN
        List<String> recipients = asList(
                "test1@gmail.com",
                "test2@gmail.com"
        );

        // WHEN
        final List<SendContact> senderEmail = sendMail.getRecipientsEmail(recipients);

        // THEN
        assertThat(senderEmail).isEqualTo(asList(
                new SendContact("test1@gmail.com"),
                new SendContact("test2@gmail.com")
        ));
    }
}
