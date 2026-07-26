package io.github.himathahangama.urbangrid.domain;

import io.github.himathahangama.urbangrid.rule.ContaminationRule;
import io.github.himathahangama.urbangrid.rule.DefaultZoningRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellTest {
    @Test
    void enforcesSwampFoundationAndMaterialConstraints() {
        Cell cell = new Cell(TerrainType.SWAMPY);

        assertFalse(cell.canBuild(new BuildingPlan(
                2,
                FoundationType.SLAB,
                MaterialType.CONCRETE)));
        assertFalse(cell.canBuild(new BuildingPlan(
                2,
                FoundationType.STILTS,
                MaterialType.WOOD)));
        assertTrue(cell.canBuild(new BuildingPlan(
                2,
                FoundationType.STILTS,
                MaterialType.BRICK)));
    }

    @Test
    void estimatesTerrainAndContaminationCosts() {
        Cell cell = new Cell(
                TerrainType.SWAMPY,
                new ContaminationRule(new DefaultZoningRule()));
        BuildingPlan plan = new BuildingPlan(
                3,
                FoundationType.STILTS,
                MaterialType.BRICK);

        BuildEstimate estimate = cell.estimate(plan);

        assertEquals(150_000.0, estimate.baseCost());
        assertEquals(1.5, estimate.zoningMultiplier());
        assertEquals(225_000.0, estimate.totalCost());
    }

    @Test
    void refusesToEstimateRejectedPlans() {
        Cell cell = new Cell(TerrainType.SWAMPY);
        BuildingPlan plan = new BuildingPlan(
                1,
                FoundationType.SLAB,
                MaterialType.WOOD);

        assertThrows(IllegalArgumentException.class, () -> cell.estimate(plan));
    }
}
