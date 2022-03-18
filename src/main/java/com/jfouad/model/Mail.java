package com.jfouad.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@ToString
public class Mail {
    @Email(message = "From field must be a well-formed email address")
    private String from;

    @Size(min = 1, message = "Recipients must have at least one address")
    @NotNull(message = "Recipients cannot be null")
    private List<@Email(message = "Recipients field must be a well-formed email address") String> to;

    private List<@Email(message = "CC field must be a well-formed email address") String> cc;

    @NotEmpty(message = "Subject cannot be empty")
    private String subject;

    @NotEmpty(message = "Body cannot be empty")
    private String body;
}
