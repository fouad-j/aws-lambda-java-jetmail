package com.jfouad;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.jfouad.model.Mail;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntryPointTest {

    final EntryPoint entryPoint = new EntryPoint();
    final Context context = mock(Context.class);

    @BeforeEach
    void setUp() {
        when(context.getLogger()).thenReturn(LambdaRuntime.getLogger());
    }

    @Test
    void should_throw_exception_when_required_fields_are_null() {
        // GIVEN
        Mail mail = new Mail();

        // WHEN
        Exception exception = assertThrows(RuntimeException.class, () -> entryPoint.handleRequest(mail, context));

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
        Exception exception = assertThrows(RuntimeException.class, () -> entryPoint.handleRequest(mail, context));

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
        Exception exception = assertThrows(RuntimeException.class, () -> entryPoint.handleRequest(mail, context));

        // THEN
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(exception.getMessage()).contains("From field must be a well-formed email address");
        soft.assertThat(exception.getMessage()).contains("Recipients field must be a well-formed email address");
        soft.assertThat(exception.getMessage()).contains("CC field must be a well-formed email address");
        soft.assertAll();
    }
}
