package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

import java.util.Locale;

public final class FloodRiskRule extends RuleDecorator {
    private final double riskPercentage;

    public FloodRiskRule(double riskPercentage, ZoningRule innerRule) {
        super(innerRule);
        if (!Double.isFinite(riskPercentage) || riskPercentage < 0 || riskPercentage > 100) {
            throw new IllegalArgumentException("Flood risk must be from 0 to 100.");
        }
        this.riskPercentage = riskPercentage;
    }

    public double riskPercentage() {
        return riskPercentage;
    }

    @Override
    public boolean allows(BuildingPlan plan) {
        return plan.floors() >= 2 && innerRule.allows(plan);
    }

    @Override
    public double costMultiplier() {
        return innerRule.costMultiplier() * (1.0 + riskPercentage / 50.0);
    }

    @Override
    public String explanation() {
        return append(
                innerRule.explanation(),
                String.format(Locale.ROOT, "at least 2 floors required at %.1f%% flood risk", riskPercentage),
                ", ");
    }

    @Override
    public String code() {
        return append(innerRule.code(), "F" + Math.round(riskPercentage), " ");
    }

    @Override
    public String description() {
        return append(
                innerRule.description(),
                String.format(Locale.ROOT, "Flood risk: %.1f%%", riskPercentage),
                "\n");
    }

    @Override
    public String warning() {
        return append(innerRule.warning(), "Flood-risk cells require at least 2 floors", "\n");
    }
}
