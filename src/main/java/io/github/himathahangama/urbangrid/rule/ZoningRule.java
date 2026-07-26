package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

/**
 * Composable zoning policy used by each grid cell.
 */
public interface ZoningRule {
    boolean allows(BuildingPlan plan);

    double costMultiplier();

    String explanation();

    String code();

    String description();

    String warning();

    boolean contaminated();
}
