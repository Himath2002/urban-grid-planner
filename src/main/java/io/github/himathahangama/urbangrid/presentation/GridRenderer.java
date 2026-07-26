package io.github.himathahangama.urbangrid.presentation;

import io.github.himathahangama.urbangrid.domain.Cell;
import io.github.himathahangama.urbangrid.domain.CityBuildResult;
import io.github.himathahangama.urbangrid.domain.CityGrid;

import java.util.Objects;

/**
 * Produces stable ASCII views without coupling the domain model to console output.
 */
public final class GridRenderer {
    private static final int CELL_WIDTH = 15;

    public String renderTerrainAndZoning(CityGrid grid) {
        Objects.requireNonNull(grid, "grid");
        StringBuilder output = new StringBuilder();
        appendDetailedBorder(output, grid.columns());

        for (int row = 0; row < grid.rows(); row++) {
            output.append('|');
            for (int column = 0; column < grid.columns(); column++) {
                Cell cell = grid.cellAt(row, column);
                output.append(String.format(" %-" + (CELL_WIDTH - 2) + "s |", cell.terrain().displayName()));
            }
            output.append('\n').append('|');
            for (int column = 0; column < grid.columns(); column++) {
                String code = grid.cellAt(row, column).zoningRule().code();
                output.append(String.format(" %-" + (CELL_WIDTH - 2) + "s |", code));
            }
            output.append('\n');
            appendDetailedBorder(output, grid.columns());
        }

        output.append("\nZoning codes: L = height limit, F = flood risk, C = contamination, H = heritage\n");
        return output.toString();
    }

    public String renderSelection(CityGrid grid, int selectedRow, int selectedColumn) {
        Objects.requireNonNull(grid, "grid");
        grid.cellAt(selectedRow, selectedColumn);
        return renderCompactGrid(
                grid,
                (row, column) -> row == selectedRow && column == selectedColumn);
    }

    public String renderBuildMap(CityGrid grid, CityBuildResult result) {
        Objects.requireNonNull(grid, "grid");
        Objects.requireNonNull(result, "result");
        return renderCompactGrid(grid, result::containsPlacement);
    }

    private String renderCompactGrid(CityGrid grid, CellMarker marker) {
        StringBuilder output = new StringBuilder();
        appendCompactBorder(output, grid.columns());

        for (int row = 0; row < grid.rows(); row++) {
            output.append('|');
            for (int column = 0; column < grid.columns(); column++) {
                output.append(marker.marked(row, column) ? " X |" : "   |");
            }
            output.append('\n');
            appendCompactBorder(output, grid.columns());
        }
        return output.toString();
    }

    private void appendDetailedBorder(StringBuilder output, int columns) {
        output.append('+').append(("-".repeat(CELL_WIDTH) + "+").repeat(columns)).append('\n');
    }

    private void appendCompactBorder(StringBuilder output, int columns) {
        output.append('+').append("---+".repeat(columns)).append('\n');
    }

    @FunctionalInterface
    private interface CellMarker {
        boolean marked(int row, int column);
    }
}
