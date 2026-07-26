package io.github.himathahangama.urbangrid.service;

import io.github.himathahangama.urbangrid.domain.BuildEstimate;
import io.github.himathahangama.urbangrid.domain.BuildPlacement;
import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.Cell;
import io.github.himathahangama.urbangrid.domain.CityBuildResult;
import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.strategy.BuildStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Applies a build strategy across a grid and records only accepted proposals.
 */
public final class CityBuilder {
    public CityBuildResult build(CityGrid grid, BuildStrategy strategy) {
        Objects.requireNonNull(grid, "grid");
        Objects.requireNonNull(strategy, "strategy");

        List<BuildPlacement> placements = new ArrayList<>();
        double totalCost = 0.0;

        for (int row = 0; row < grid.rows(); row++) {
            for (int column = 0; column < grid.columns(); column++) {
                BuildingPlan plan = strategy.generate(
                        row,
                        column,
                        grid.rows(),
                        grid.columns());
                Cell cell = grid.cellAt(row, column);

                if (cell.canBuild(plan)) {
                    BuildEstimate estimate = cell.estimate(plan);
                    placements.add(new BuildPlacement(row, column, plan, estimate.totalCost()));
                    totalCost += estimate.totalCost();
                }
            }
        }

        return new CityBuildResult(placements, totalCost);
    }
}
