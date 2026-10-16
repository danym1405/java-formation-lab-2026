package com.indra.notifications;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("dev")
class NotificationApplicationTests {

    @Autowired 
    private EmailSender emailSender;

    @Test
    void shouldLoadFakeEmailSender() {
        assertTrue(emailSender instanceof FakeEmailSender);
    }
}
