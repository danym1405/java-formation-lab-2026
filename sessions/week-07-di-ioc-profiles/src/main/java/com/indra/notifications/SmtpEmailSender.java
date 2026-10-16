package com.indra.notifications;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service 
@Profile("prod")
public class SmtpEmailSender implements EmailSender {

    @Override
    public void send(String to, String subject, String body) {
        System.out.println("SMTP email sent to " + to + " with subject: " + subject + " and body: " + body);
    }
}
