package io.github.himathahangama.urbangrid.domain;

import java.util.Locale;

public enum MaterialType {
    WOOD(10_000.0),
    STONE(50_000.0),
    BRICK(30_000.0),
    CONCRETE(20_000.0);

    private final double costPerFloor;

    MaterialType(double costPerFloor) {
        this.costPerFloor = costPerFloor;
    }

    public double costPerFloor() {
        return costPerFloor;
    }

    public static MaterialType parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Material type is required.");
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported material type: " + value, exception);
        }
    }

    public String displayName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
