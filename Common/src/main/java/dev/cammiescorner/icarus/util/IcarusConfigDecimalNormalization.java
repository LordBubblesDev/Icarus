package dev.cammiescorner.icarus.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Normalize {@code digit,digit} sequences to {@code digit.digit} in the on-disk file so the config screen and JSON stay consistent.
 */
public final class IcarusConfigDecimalNormalization {

    private static final Pattern DECIMAL_COMMA_BETWEEN_DIGITS = Pattern.compile("([0-9]+),([0-9]+)");

    private IcarusConfigDecimalNormalization() {
    }

    /**
     * Replaces comma decimal separators with dots for simple numeric forms (e.g. {@code 0,04} → {@code 0.04}).
     */
    public static String normalizeDecimalSeparatorsInText(String content) {
        String result = content;
        String prev;
        do {
            prev = result;
            result = DECIMAL_COMMA_BETWEEN_DIGITS.matcher(result).replaceAll("$1.$2");
        } while (!result.equals(prev));
        return result;
    }

    public static void normalizeFileIfPresent(Path file) {
        if (!Files.isRegularFile(file)) {
            return;
        }
        try {
            String original = Files.readString(file, StandardCharsets.UTF_8);
            String normalized = normalizeDecimalSeparatorsInText(original);
            if (!normalized.equals(original)) {
                Files.writeString(file, normalized, StandardCharsets.UTF_8);
            }
        } catch (IOException ignored) {
            // Best-effort; do not break config load/save
        }
    }
}
