package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.MaterialType;

import java.util.Objects;

public final class HeritageRule extends RuleDecorator {
    private final MaterialType requiredMaterial;

    public HeritageRule(MaterialType requiredMaterial, ZoningRule innerRule) {
        super(innerRule);
        this.requiredMaterial = Objects.requireNonNull(requiredMaterial, "requiredMaterial");
    }

    public MaterialType requiredMaterial() {
        return requiredMaterial;
    }

    @Override
    public boolean allows(BuildingPlan plan) {
        return plan.material() == requiredMaterial && innerRule.allows(plan);
    }

    @Override
    public String explanation() {
        return append(
                innerRule.explanation(),
                "heritage material must be " + requiredMaterial.displayName(),
                ", ");
    }

    @Override
    public String code() {
        return append(innerRule.code(), "H" + requiredMaterial.name().charAt(0), " ");
    }

    @Override
    public String description() {
        return append(
                innerRule.description(),
                "Heritage material: " + requiredMaterial.displayName(),
                "\n");
    }

    @Override
    public String warning() {
        return append(
                innerRule.warning(),
                "Material must be " + requiredMaterial.displayName() + " in this heritage zone",
                "\n");
    }
}
