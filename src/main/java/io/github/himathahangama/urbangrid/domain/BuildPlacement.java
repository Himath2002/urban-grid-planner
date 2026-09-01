package io.github.himathahangama.urbangrid.domain;

import java.util.Locale;
import java.util.Objects;

public record BuildPlacement(int row, int column, BuildingPlan plan, double cost) {
    public BuildPlacement {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Grid coordinates cannot be negative.");
        }
        Objects.requireNonNull(plan, "plan");
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }
    }

    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "row %d, column %d - %d floors, %s, %s ($%,.2f)",
                row + 1,
                column + 1,
                plan.floors(),
                plan.foundation().displayName(),
                plan.material().displayName(),
                cost);
    }
}
