package com.qiu.apifinal.entity.dto;


import lombok.Data;

@Data
public class EmailMessage {
    String email;
    String subject;
    String text;

    public EmailMessage(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }
}
