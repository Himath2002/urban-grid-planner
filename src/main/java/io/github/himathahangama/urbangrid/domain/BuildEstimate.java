package io.github.himathahangama.urbangrid.domain;

public record BuildEstimate(double baseCost, double zoningMultiplier, double totalCost) {
    public BuildEstimate {
        if (baseCost < 0 || zoningMultiplier <= 0 || totalCost < 0) {
            throw new IllegalArgumentException("Build costs and multipliers must be positive.");
        }
    }
}
