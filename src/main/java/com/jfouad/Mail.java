package com.jfouad;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Mail {
    private String from;
    private List<String> to;
    private List<String> cc;
    private String subject;
    private String body;
}
