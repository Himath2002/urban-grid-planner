package io.github.himathahangama.urbangrid.strategy;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;

/**
 * Strategy contract for generating one proposal at a grid coordinate.
 */
@FunctionalInterface
public interface BuildStrategy {
    BuildingPlan generate(int row, int column, int rows, int columns);
}
