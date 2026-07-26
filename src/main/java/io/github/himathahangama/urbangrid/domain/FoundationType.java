package io.github.himathahangama.urbangrid.domain;

import java.util.Locale;

public enum FoundationType {
    SLAB,
    STILTS;

    public static FoundationType parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Foundation type is required.");
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported foundation type: " + value, exception);
        }
    }

    public String displayName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
