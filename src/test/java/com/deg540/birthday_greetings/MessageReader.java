package com.deg540.birthday_greetings;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class MessageReader {
    public static Message[] messagesSent() throws IOException {
        String jsonString = IOUtils.toString(
                new URL("http://127.0.0.1:8025/api/v1/messages"),
                Charset.defaultCharset());

        return new Gson().fromJson(jsonString, Message[].class);
    }

    public static class Headers {
        String[] Subject;
        String[] To;
    }

    public static class Content {
        String Body;
        Headers Headers;
    }

    public static class Message {
        public Content Content;
    }
}
