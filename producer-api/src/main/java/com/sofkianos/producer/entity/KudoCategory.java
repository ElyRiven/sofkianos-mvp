package com.sofkianos.producer.entity;

import java.util.Arrays;

/**
 * Enum representing the valid categories for a Kudo in the producer API.
 */
public enum KudoCategory {

    Innovation,
    Teamwork,
    Passion,
    Mastery;

    /**
     * Case-insensitive factory method.
     *
     * @param text the raw category string
     * @return the matching {@link KudoCategory}
     * @throws IllegalArgumentException if no match is found
     */
    public static KudoCategory fromString(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Category must not be null or empty");
        }
        return Arrays.stream(values())
                .filter(c -> c.name().equalsIgnoreCase(text.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Unknown KudoCategory: '%s'. Valid values: %s",
                                text, Arrays.toString(values()))));
    }
}
