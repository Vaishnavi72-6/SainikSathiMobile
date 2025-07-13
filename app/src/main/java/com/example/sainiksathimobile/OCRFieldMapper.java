package com.example.sainiksathimobile;

import java.util.HashMap;
import java.util.Map;

public class OCRFieldMapper {

    public static Map<String, String> extractFields(String rawText) {
        Map<String, String> fields = new HashMap<>();
        String[] lines = rawText.split("\\n");

        for (String line : lines) {
            line = line.toLowerCase();

            if (line.contains("name")) {
                fields.put("name", extractValue(line));
            } else if (line.contains("dob") || line.contains("date of birth")) {
                fields.put("dob", extractValue(line));
            } else if (line.contains("rank")) {
                fields.put("rank", extractValue(line));
            } else if (line.contains("ppo")) {
                fields.put("ppo", extractValue(line));
            } else if (line.contains("aadhaar")) {
                fields.put("aadhaar", extractValue(line));
            } else if (line.contains("email")) {
                fields.put("email", extractValue(line));
            }
        }

        return fields;
    }

    private static String extractValue(String line) {
        String[] parts = line.split(":");
        return parts.length > 1 ? parts[1].trim() : "";
    }
}
