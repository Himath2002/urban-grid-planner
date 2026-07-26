package io.github.himathahangama.urbangrid.domain;

import io.github.himathahangama.urbangrid.rule.DefaultZoningRule;
import io.github.himathahangama.urbangrid.rule.ZoningRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * One parcel in the city grid, including its immutable terrain and composed zoning rule.
 */
public final class Cell {
    private final TerrainType terrain;
    private ZoningRule zoningRule;

    public Cell(TerrainType terrain) {
        this(terrain, new DefaultZoningRule());
    }

    public Cell(TerrainType terrain, ZoningRule zoningRule) {
        this.terrain = Objects.requireNonNull(terrain, "terrain");
        this.zoningRule = Objects.requireNonNull(zoningRule, "zoningRule");
    }

    public TerrainType terrain() {
        return terrain;
    }

    public ZoningRule zoningRule() {
        return zoningRule;
    }

    public void setZoningRule(ZoningRule zoningRule) {
        this.zoningRule = Objects.requireNonNull(zoningRule, "zoningRule");
    }

    public boolean canBuild(BuildingPlan plan) {
        return rejectionReasons(plan).isEmpty();
    }

    public List<String> rejectionReasons(BuildingPlan plan) {
        Objects.requireNonNull(plan, "plan");
        List<String> reasons = new ArrayList<>();

        if (terrain == TerrainType.SWAMPY && plan.foundation() == FoundationType.SLAB) {
            reasons.add("slab foundations are not supported on swampy terrain");
        }
        if (terrain == TerrainType.SWAMPY && plan.material() == MaterialType.WOOD) {
            reasons.add("wood structures are not supported on swampy terrain");
        }
        if (!zoningRule.allows(plan)) {
            String explanation = zoningRule.explanation();
            reasons.add(explanation.isBlank()
                    ? "zoning rules reject this plan"
                    : explanation);
        }

        return List.copyOf(reasons);
    }

    public BuildEstimate estimate(BuildingPlan plan) {
        List<String> rejectionReasons = rejectionReasons(plan);
        if (!rejectionReasons.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot estimate a rejected plan: " + String.join("; ", rejectionReasons));
        }

        double baseCost = plan.material().costPerFloor() * plan.floors();
        if (terrain == TerrainType.SWAMPY) {
            baseCost += 20_000.0 * plan.floors();
        } else if (terrain == TerrainType.ROCKY) {
            baseCost += 50_000.0;
        }

        double multiplier = zoningRule.costMultiplier();
        return new BuildEstimate(baseCost, multiplier, baseCost * multiplier);
    }
}
