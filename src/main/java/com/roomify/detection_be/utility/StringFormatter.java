package com.roomify.detection_be.utility;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class StringFormatter {
    private static final PropertyNamingStrategies.SnakeCaseStrategy SNAKE_CASE_STRATEGY =
            new PropertyNamingStrategies.SnakeCaseStrategy();

    public static String translateCamelCaseToHumanReadable(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String snakeCase = SNAKE_CASE_STRATEGY.translate(str);

        String[] words = snakeCase.split("_");

        StringBuilder humanReadable = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                humanReadable.append(word.substring(0, 1).toUpperCase());
                humanReadable.append(word.substring(1));
                humanReadable.append(" ");
            }
        }

        return humanReadable.toString().trim();
    }
}
