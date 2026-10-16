package com.indra.notifications;

public interface EmailSender {

    void send(String to, String subject, String body);

}
