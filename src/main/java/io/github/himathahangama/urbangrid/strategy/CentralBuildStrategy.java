package io.github.himathahangama.urbangrid.strategy;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.FoundationType;
import io.github.himathahangama.urbangrid.domain.MaterialType;

public final class CentralBuildStrategy implements BuildStrategy {
    @Override
    public BuildingPlan generate(int row, int column, int rows, int columns) {
        double centreRow = (rows - 1) / 2.0;
        double centreColumn = (columns - 1) / 2.0;
        double distance = Math.hypot(row - centreRow, column - centreColumn);
        int floors = (int) Math.round(1.0 + 20.0 / (distance + 1.0));

        MaterialType material;
        if (distance <= 2.0) {
            material = MaterialType.CONCRETE;
        } else if (distance <= 4.0) {
            material = MaterialType.BRICK;
        } else if (distance <= 6.0) {
            material = MaterialType.STONE;
        } else {
            material = MaterialType.WOOD;
        }

        return new BuildingPlan(floors, FoundationType.SLAB, material);
    }
}
