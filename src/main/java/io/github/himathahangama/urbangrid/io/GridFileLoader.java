package io.github.himathahangama.urbangrid.io;

import io.github.himathahangama.urbangrid.domain.Cell;
import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.domain.MaterialType;
import io.github.himathahangama.urbangrid.domain.TerrainType;
import io.github.himathahangama.urbangrid.rule.ContaminationRule;
import io.github.himathahangama.urbangrid.rule.DefaultZoningRule;
import io.github.himathahangama.urbangrid.rule.FloodRiskRule;
import io.github.himathahangama.urbangrid.rule.HeightLimitRule;
import io.github.himathahangama.urbangrid.rule.HeritageRule;
import io.github.himathahangama.urbangrid.rule.ZoningRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Strict UTF-8 parser for the documented terrain-and-zoning grid format.
 */
public final class GridFileLoader {
    public static final int MAXIMUM_CELLS = 100_000;

    public CityGrid load(Path path) throws IOException {
        Objects.requireNonNull(path, "path");

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String dimensionLine = reader.readLine();
            int[] dimensions = parseDimensions(dimensionLine);
            int rows = dimensions[0];
            int columns = dimensions[1];
            int expectedCells = Math.multiplyExact(rows, columns);

            if (expectedCells > MAXIMUM_CELLS) {
                throw new IllegalArgumentException(
                        "Grid contains " + expectedCells + " cells; maximum is " + MAXIMUM_CELLS + ".");
            }

            List<Cell> cells = new ArrayList<>(expectedCells);
            for (int index = 0; index < expectedCells; index++) {
                String line = reader.readLine();
                if (line == null || line.isBlank()) {
                    throw new IllegalArgumentException(
                            "Missing record for cell " + (index + 1) + " of " + expectedCells + ".");
                }
                cells.add(parseCell(line, index + 1));
            }

            String remainingLine;
            while ((remainingLine = reader.readLine()) != null) {
                if (!remainingLine.isBlank()) {
                    throw new IllegalArgumentException(
                            "Unexpected data after the expected " + expectedCells + " cell records.");
                }
            }

            return new CityGrid(cells, rows, columns);
        }
    }

    private int[] parseDimensions(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Grid file is empty or missing dimensions.");
        }

        String[] values = line.split(",", -1);
        if (values.length != 2) {
            throw new IllegalArgumentException("Grid dimensions must use 'rows,columns'.");
        }

        try {
            int rows = Integer.parseInt(values[0].trim());
            int columns = Integer.parseInt(values[1].trim());
            if (rows < 1 || columns < 1) {
                throw new IllegalArgumentException("Grid dimensions must be positive.");
            }
            return new int[]{rows, columns};
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Grid dimensions must be whole numbers.", exception);
        }
    }

    private Cell parseCell(String line, int cellNumber) {
        String[] tokens = line.split(",", -1);
        TerrainType terrain;
        try {
            terrain = TerrainType.parse(tokens[0]);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid terrain in cell " + cellNumber + ": " + tokens[0],
                    exception);
        }

        ZoningRule rule = new DefaultZoningRule();
        for (int index = 1; index < tokens.length; index++) {
            rule = parseRule(tokens[index], rule, cellNumber);
        }

        return new Cell(terrain, rule);
    }

    private ZoningRule parseRule(String token, ZoningRule innerRule, int cellNumber) {
        String[] pair = token.trim().split("=", -1);
        if (pair.length > 2 || pair[0].isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid zoning rule in cell " + cellNumber + ": " + token);
        }

        String key = pair[0].trim().toLowerCase(Locale.ROOT);
        String value = pair.length == 2 ? pair[1].trim() : "";

        return switch (key) {
            case "heritage" -> new HeritageRule(
                    parseMaterial(value, cellNumber),
                    innerRule);
            case "height-limit" -> new HeightLimitRule(
                    parsePositiveInteger(value, "height-limit", cellNumber),
                    innerRule);
            case "flood-risk" -> new FloodRiskRule(
                    parseFloodRisk(value, cellNumber),
                    innerRule);
            case "contamination" -> {
                if (!value.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Contamination does not accept a value in cell " + cellNumber + ".");
                }
                yield new ContaminationRule(innerRule);
            }
            default -> throw new IllegalArgumentException(
                    "Unknown zoning rule in cell " + cellNumber + ": " + key);
        };
    }

    private MaterialType parseMaterial(String value, int cellNumber) {
        try {
            return MaterialType.parse(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid heritage material in cell " + cellNumber + ": " + value,
                    exception);
        }
    }

    private int parsePositiveInteger(String value, String field, int cellNumber) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 1) {
                throw new IllegalArgumentException(
                        field + " must be positive in cell " + cellNumber + ".");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    field + " must be a whole number in cell " + cellNumber + ": " + value,
                    exception);
        }
    }

    private double parseFloodRisk(String value, int cellNumber) {
        try {
            double risk = Double.parseDouble(value);
            if (!Double.isFinite(risk) || risk < 0 || risk > 100) {
                throw new IllegalArgumentException(
                        "flood-risk must be from 0 to 100 in cell " + cellNumber + ".");
            }
            return risk;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "flood-risk must be numeric in cell " + cellNumber + ": " + value,
                    exception);
        }
    }
}
