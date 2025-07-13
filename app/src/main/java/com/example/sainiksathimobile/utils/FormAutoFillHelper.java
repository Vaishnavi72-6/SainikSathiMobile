package com.example.sainiksathimobile.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormAutoFillHelper {

    public static String extractField(String text, String field) {
        if (text == null || text.isEmpty() || field == null) return "Not Found";

        Pattern pattern;
        switch (field.toLowerCase()) {
            case "ppo":
                pattern = Pattern.compile("PPO\\s*(No)?[:\\-\\s]*([A-Z0-9]+)", Pattern.CASE_INSENSITIVE);
                break;

            case "dob":
                // Match DD/MM/YYYY or DD-MM-YYYY
                pattern = Pattern.compile("\\b(\\d{2}[/-]\\d{2}[/-]\\d{4})\\b");
                break;

            case "name":
                // Match line like: Name: Vaishnavi Reddy
                pattern = Pattern.compile("Name[:\\-\\s]*([A-Za-z\\s\\.]+)", Pattern.CASE_INSENSITIVE);
                break;

            case "rank":
                // Match: Rank: Lieutenant or similar
                pattern = Pattern.compile("Rank[:\\-\\s]*([A-Za-z\\s\\.]+)", Pattern.CASE_INSENSITIVE);
                break;

            default:
                return "Not Found";
        }

        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(matcher.groupCount()).trim();
        }

        return "Not Found";
    }
}
