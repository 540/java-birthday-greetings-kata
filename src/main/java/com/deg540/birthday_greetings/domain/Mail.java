package com.deg540.birthday_greetings.domain;

public class Mail {
    String recipient;
    String body;
    String subject;

    public Mail(String recipient, String body, String subject) {
        this.recipient = recipient;
        this.body = body;
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }
}
