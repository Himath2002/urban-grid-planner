package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

public final class DefaultZoningRule implements ZoningRule {
    @Override
    public boolean allows(BuildingPlan plan) {
        return true;
    }

    @Override
    public double costMultiplier() {
        return 1.0;
    }

    @Override
    public String explanation() {
        return "";
    }

    @Override
    public String code() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String warning() {
        return "";
    }

    @Override
    public boolean contaminated() {
        return false;
    }
}
