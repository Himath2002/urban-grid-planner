package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

public final class ContaminationRule extends RuleDecorator {
    private static final double COST_MULTIPLIER = 1.5;

    public ContaminationRule(ZoningRule innerRule) {
        super(innerRule);
    }

    @Override
    public boolean allows(BuildingPlan plan) {
        return innerRule.allows(plan);
    }

    @Override
    public double costMultiplier() {
        return innerRule.costMultiplier() * COST_MULTIPLIER;
    }

    @Override
    public String explanation() {
        return innerRule.explanation();
    }

    @Override
    public String code() {
        return append(innerRule.code(), "C", " ");
    }

    @Override
    public String description() {
        return append(innerRule.description(), "Contamination: 1.5x cost", "\n");
    }

    @Override
    public boolean contaminated() {
        return true;
    }
}
