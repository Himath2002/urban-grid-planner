package io.github.himathahangama.urbangrid.domain;

import java.util.Objects;

/**
 * Immutable structure proposal evaluated against terrain and zoning constraints.
 */
public record BuildingPlan(int floors, FoundationType foundation, MaterialType material) {
    public BuildingPlan {
        if (floors < 1) {
            throw new IllegalArgumentException("Floors must be positive.");
        }
        Objects.requireNonNull(foundation, "foundation");
        Objects.requireNonNull(material, "material");
    }
}
