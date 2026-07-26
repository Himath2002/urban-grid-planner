package io.github.himathahangama.urbangrid.presentation;

import io.github.himathahangama.urbangrid.domain.Cell;
import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.domain.TerrainType;
import io.github.himathahangama.urbangrid.rule.DefaultZoningRule;
import io.github.himathahangama.urbangrid.rule.HeightLimitRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GridRendererTest {
    @Test
    void rendersStableTerrainAndSelectionViews() {
        CityGrid grid = new CityGrid(
                List.of(
                        new Cell(TerrainType.FLAT),
                        new Cell(
                                TerrainType.ROCKY,
                                new HeightLimitRule(6, new DefaultZoningRule()))),
                1,
                2);
        GridRenderer renderer = new GridRenderer();

        String detailed = renderer.renderTerrainAndZoning(grid);
        String selection = renderer.renderSelection(grid, 0, 1);

        assertTrue(detailed.contains("flat"));
        assertTrue(detailed.contains("rocky"));
        assertTrue(detailed.contains("L6"));
        assertEquals("""
                +---+---+
                |   | X |
                +---+---+
                """, selection);
    }
}
