package com.example.cookbook.model;

public class ChatMessage {
    public String sender; // "user" ili "ai"
    public String text;

    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }
}

