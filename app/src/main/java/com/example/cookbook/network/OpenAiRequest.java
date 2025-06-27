package com.example.cookbook.network;

import java.util.List;

public class OpenAiRequest {
    public String model;
    public List<Message> messages;

    public static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    public OpenAiRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }
}
