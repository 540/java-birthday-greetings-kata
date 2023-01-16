package com.deg540.birthday_greetings;

import com.deg540.birthday_greetings.domain.OurDate;
import com.deg540.birthday_greetings.infrastructure.FileSystemEmployeeRepository;
import com.deg540.birthday_greetings.infrastructure.TransportMailer;
import com.deg540.birthday_greetings.services.BirthdayService;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

import static com.deg540.birthday_greetings.MessageReader.messagesSent;
import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class AcceptanceTest {
    private BirthdayService service;

    @Before
    public void setUp() throws Exception {
        service = new BirthdayService(new FileSystemEmployeeRepository(), new TransportMailer());

        deleteAllMailsFromMailhog();
    }

    @Test
    public void baseScenario() throws Exception {
        service.sendGreetings(new OurDate("2008/10/08"));

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
        service.sendGreetings(new OurDate("2008/01/01"));

        assertEquals("what? messages?", 0, messagesSent().length);
    }

    private static void deleteAllMailsFromMailhog() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpget = new HttpDelete("http://127.0.0.1:8025/api/v1/messages");
        httpclient.execute(httpget);
    }
}
