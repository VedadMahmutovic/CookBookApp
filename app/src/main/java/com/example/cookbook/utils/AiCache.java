package com.example.cookbook.utils;

import java.util.HashMap;
import java.util.Map;

public class AiCache {
    private static final Map<String, String> cache = new HashMap<>();

    public static boolean has(String key) {
        return cache.containsKey(key);
    }

    public static String get(String key) {
        return cache.get(key);
    }

    public static void put(String key, String value) {
        cache.put(key, value);
    }
}
