package io.github.himathahangama.urbangrid.domain;

import java.util.List;
import java.util.Objects;

/**
 * Validated row-major city grid.
 */
public final class CityGrid {
    private final List<Cell> cells;
    private final int rows;
    private final int columns;

    public CityGrid(List<Cell> cells, int rows, int columns) {
        Objects.requireNonNull(cells, "cells");
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("Grid dimensions must be positive.");
        }

        int expectedSize = Math.multiplyExact(rows, columns);
        if (cells.size() != expectedSize || cells.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Grid must contain exactly " + expectedSize + " non-null cells.");
        }

        this.cells = List.copyOf(cells);
        this.rows = rows;
        this.columns = columns;
    }

    public List<Cell> cells() {
        return cells;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public Cell cellAt(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException(
                    "Cell (" + row + ", " + column + ") is outside the grid.");
        }
        return cells.get(row * columns + column);
    }
}
