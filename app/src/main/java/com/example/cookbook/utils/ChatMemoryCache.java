package com.example.cookbook.utils;

import com.example.cookbook.model.ChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ChatMemoryCache {

    private static final HashMap<Integer, List<ChatMessage>> memory = new HashMap<>();

    public static List<ChatMessage> get(int recipeId) {
        return memory.getOrDefault(recipeId, new ArrayList<>());
    }

    public static void put(int recipeId, List<ChatMessage> messages) {
        memory.put(recipeId, messages);
    }

    public static void clear(int recipeId) {
        memory.remove(recipeId);
    }
}
