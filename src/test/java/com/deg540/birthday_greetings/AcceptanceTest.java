package com.deg540.birthday_greetings;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static com.deg540.birthday_greetings.MessageReader.messagesSent;
import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class AcceptanceTest {

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
            .file("src/test/resources/docker-compose.yaml")
            .waitingForService("mailhog", HealthChecks.toHaveAllPortsOpen())
            .build();

    private static final String SMTP_HOST = "127.0.0.1";
    private static final int SMTP_PORT = 1025;

    private BirthdayService service;

    @Before
    public void setUp() throws Exception {
        service = new BirthdayService();
    }

    @Test
    public void baseScenario() throws Exception {
        service.sendGreetings("src/test/resources/employee_data.txt",
                new OurDate("2008/10/08"), SMTP_HOST, SMTP_PORT);

        MessageReader.Message[] messages = messagesSent();
        assertEquals("message not sent?", 1, messages.length);
        MessageReader.Content message = messages[0].Content;
        assertEquals("Happy Birthday, dear John!", message.Body);
        assertEquals("Happy Birthday!", message.Headers.Subject[0]);
        assertEquals(1, message.Headers.To.length);
        assertEquals("john.doe@foobar.com", message.Headers.To[0]);
    }

    @Test
    public void willNotSendEmailsWhenNobodysBirthday() throws Exception {
        service.sendGreetings("src/test/resources/employee_data.txt",
                new OurDate("2008/01/01"), SMTP_HOST, SMTP_PORT);

        assertEquals("what? messages?", 0, messagesSent().length);
    }
}
