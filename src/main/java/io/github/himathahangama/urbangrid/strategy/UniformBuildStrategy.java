package io.github.himathahangama.urbangrid.strategy;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

import java.util.Objects;

public final class UniformBuildStrategy implements BuildStrategy {
    private final BuildingPlan plan;

    public UniformBuildStrategy(BuildingPlan plan) {
        this.plan = Objects.requireNonNull(plan, "plan");
    }

    @Override
    public BuildingPlan generate(int row, int column, int rows, int columns) {
        return plan;
    }
}
