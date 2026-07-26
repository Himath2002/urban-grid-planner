package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

public final class HeightLimitRule extends RuleDecorator {
    private final int maximumFloors;

    public HeightLimitRule(int maximumFloors, ZoningRule innerRule) {
        super(innerRule);
        if (maximumFloors < 1) {
            throw new IllegalArgumentException("Height limit must be positive.");
        }
        this.maximumFloors = maximumFloors;
    }

    public int maximumFloors() {
        return maximumFloors;
    }

    @Override
    public boolean allows(BuildingPlan plan) {
        return plan.floors() <= maximumFloors && innerRule.allows(plan);
    }

    @Override
    public String explanation() {
        return append(innerRule.explanation(), "maximum " + maximumFloors + " floors", ", ");
    }

    @Override
    public String code() {
        return append(innerRule.code(), "L" + maximumFloors, " ");
    }

    @Override
    public String description() {
        return append(innerRule.description(), "Height limit: " + maximumFloors + " floors", "\n");
    }

    @Override
    public String warning() {
        return append(innerRule.warning(), "Structure cannot exceed " + maximumFloors + " floors", "\n");
    }
}
