package io.github.himathahangama.urbangrid.service;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.Cell;
import io.github.himathahangama.urbangrid.domain.CityBuildResult;
import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.domain.FoundationType;
import io.github.himathahangama.urbangrid.domain.MaterialType;
import io.github.himathahangama.urbangrid.domain.TerrainType;
import io.github.himathahangama.urbangrid.strategy.UniformBuildStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CityBuilderTest {
    @Test
    void recordsOnlyPlansAcceptedByEachCell() {
        CityGrid grid = new CityGrid(
                List.of(
                        new Cell(TerrainType.FLAT),
                        new Cell(TerrainType.SWAMPY),
                        new Cell(TerrainType.ROCKY)),
                1,
                3);
        BuildingPlan plan = new BuildingPlan(
                2,
                FoundationType.SLAB,
                MaterialType.CONCRETE);

        CityBuildResult result = new CityBuilder().build(
                grid,
                new UniformBuildStrategy(plan));

        assertEquals(2, result.structureCount());
        assertEquals(130_000.0, result.totalCost());
        assertTrue(result.containsPlacement(0, 0));
        assertFalse(result.containsPlacement(0, 1));
        assertTrue(result.containsPlacement(0, 2));
    }
}
