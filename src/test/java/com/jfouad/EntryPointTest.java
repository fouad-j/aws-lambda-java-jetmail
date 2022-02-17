package com.jfouad;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntryPointTest {

    EntryPoint entryPoint = new EntryPoint();

    @Test
    void should_throw_exception_when_required_fields_are_null() {
        // GIVEN
        Mail mail = new Mail();

        // WHEN
        Exception exception = assertThrows(RuntimeException.class, () -> entryPoint.main(mail));

        // THEN
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(exception.getMessage()).contains("Body cannot be empty");
        soft.assertThat(exception.getMessage()).contains("Recipients cannot be null");
        soft.assertThat(exception.getMessage()).contains("Subject cannot be empty");
        soft.assertAll();
    }

    @Test
    void should_throw_exception_when_recipients_list_is_empty() {
        // GIVEN
        Mail mail = new Mail()
                .setSubject("Hello")
                .setBody("Body")
                .setTo(emptyList());

        // WHEN
        Exception exception = assertThrows(RuntimeException.class, () -> entryPoint.main(mail));

        // THEN
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(exception.getMessage()).contains("Recipients must have at least one address");
        soft.assertAll();
    }

    @Test
    void should_throw_exception_when_recipients_list_is_empyty() {
        // GIVEN
        Mail mail = new Mail()
                .setSubject("Hello")
                .setBody("Body")
                .setFrom("lorem_ipsum")
                .setTo(asList("test@domain.com", "lorem_ipsum"))
                .setCc(asList("test@domain.com", "test2@domain.com", "lorem_ipsum"));

        // WHEN
        Exception exception = assertThrows(RuntimeException.class, () -> entryPoint.main(mail));

        // THEN
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(exception.getMessage()).contains("From field must be a well-formed email address");
        soft.assertThat(exception.getMessage()).contains("Recipients field must be a well-formed email address");
        soft.assertThat(exception.getMessage()).contains("CC field must be a well-formed email address");
        soft.assertAll();
    }
}
