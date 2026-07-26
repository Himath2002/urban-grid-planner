package io.github.himathahangama.urbangrid.domain;

import java.util.Locale;

public enum TerrainType {
    FLAT,
    SWAMPY,
    ROCKY;

    public static TerrainType parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Terrain type is required.");
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported terrain type: " + value, exception);
        }
    }

    public String displayName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
