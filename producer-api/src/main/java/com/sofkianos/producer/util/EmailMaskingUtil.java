package com.sofkianos.producer.util;

/**
 * Utility class for masking email addresses into display-safe names.
 */
public final class EmailMaskingUtil {

    private EmailMaskingUtil() {
        // utility class
    }

    /**
     * Converts an email address local part into a display name.
     *
     * <p>For example, {@code "juan.perez@sofka.com"} becomes {@code "Juan Perez"}.
     * Splits the local part by {@code .}, {@code _}, or {@code -} and title-cases each token.</p>
     *
     * @param email the email address to convert; may be {@code null} or blank
     * @return the display name, or {@code "Anónimo"} if email is null or blank
     */
    public static String toDisplayName(String email) {
        if (email == null || email.isBlank()) {
            return "Anónimo";
        }
        String localPart = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String[] tokens = localPart.split("[._\\-]");
        StringBuilder name = new StringBuilder();
        for (String token : tokens) {
            if (!token.isBlank()) {
                if (!name.isEmpty()) {
                    name.append(' ');
                }
                name.append(Character.toUpperCase(token.charAt(0)));
                if (token.length() > 1) {
                    name.append(token.substring(1).toLowerCase());
                }
            }
        }
        return name.isEmpty() ? "Anónimo" : name.toString();
    }
}
