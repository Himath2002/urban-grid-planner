package io.github.himathahangama.urbangrid.application;

import io.github.himathahangama.urbangrid.domain.BuildEstimate;
import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.Cell;
import io.github.himathahangama.urbangrid.domain.CityBuildResult;
import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.domain.FoundationType;
import io.github.himathahangama.urbangrid.domain.MaterialType;
import io.github.himathahangama.urbangrid.presentation.GridRenderer;
import io.github.himathahangama.urbangrid.service.CityBuilder;
import io.github.himathahangama.urbangrid.strategy.BuildStrategy;
import io.github.himathahangama.urbangrid.strategy.CentralBuildStrategy;
import io.github.himathahangama.urbangrid.strategy.RandomBuildStrategy;
import io.github.himathahangama.urbangrid.strategy.UniformBuildStrategy;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Owns the interactive console workflow while domain and service classes remain UI-independent.
 */
public final class ConsoleSession {
    private static final Logger LOGGER = Logger.getLogger(ConsoleSession.class.getName());

    private final CityGrid grid;
    private final GridRenderer renderer;
    private final Scanner input;
    private final PrintStream output;
    private final CityBuilder cityBuilder;
    private BuildMode buildMode = BuildMode.RANDOM;

    public ConsoleSession(CityGrid grid, GridRenderer renderer, InputStream input, PrintStream output) {
        this.grid = Objects.requireNonNull(grid, "grid");
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.input = new Scanner(Objects.requireNonNull(input, "input"));
        this.output = Objects.requireNonNull(output, "output");
        this.cityBuilder = new CityBuilder();
    }

    public void run() {
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                running = handleMainMenu(readIntInRange("Select an option (1-4): ", 1, 4));
            }
        } catch (NoSuchElementException exception) {
            LOGGER.fine("Input stream closed; ending console session.");
            output.println("\nInput closed. Exiting UrbanGrid Planner.");
        }
    }

    private void printMainMenu() {
        output.println("\n=== UrbanGrid Planner ===");
        output.println("1. Evaluate one structure");
        output.printf("2. Simulate city build (mode: %s)%n", buildMode.displayName());
        output.println("3. Configure build mode");
        output.println("4. Quit");
    }

    private boolean handleMainMenu(int choice) {
        switch (choice) {
            case 1 -> evaluateStructure();
            case 2 -> simulateCity();
            case 3 -> configureBuildMode();
            case 4 -> {
                output.println("Goodbye.");
                return false;
            }
            default -> throw new IllegalStateException("Unsupported menu choice: " + choice);
        }
        return true;
    }

    private void evaluateStructure() {
        output.println("\n=== Structure evaluation ===");
        int row = readIntInRange("Row (1-" + grid.rows() + "): ", 1, grid.rows()) - 1;
        int column = readIntInRange("Column (1-" + grid.columns() + "): ", 1, grid.columns()) - 1;
        BuildingPlan plan = readBuildingPlan();
        Cell cell = grid.cellAt(row, column);
        List<String> rejectionReasons = cell.rejectionReasons(plan);

        if (!rejectionReasons.isEmpty()) {
            output.println("\nConstruction rejected:");
            rejectionReasons.forEach(reason -> output.println("- " + reason));
            output.println(renderer.renderSelection(grid, row, column));
            return;
        }

        BuildEstimate estimate = cell.estimate(plan);
        output.println("\nConstruction approved.");
        output.printf("- Base cost: $%,.2f%n", estimate.baseCost());
        output.printf("- Zoning multiplier: %.2fx%n", estimate.zoningMultiplier());
        output.printf("- Total estimate: $%,.2f%n", estimate.totalCost());
        output.printf(
                "- Plan: %d floors, %s foundation, %s%n",
                plan.floors(),
                plan.foundation().displayName(),
                plan.material().displayName());
        output.printf("- Terrain: %s%n", cell.terrain().displayName());

        if (!cell.zoningRule().description().isBlank()) {
            output.println("- Zoning: " + cell.zoningRule().description().replace("\n", "; "));
        }
        if (!cell.zoningRule().warning().isBlank()) {
            output.println("- Warning: " + cell.zoningRule().warning().replace("\n", "; "));
        }

        output.println(renderer.renderSelection(grid, row, column));
    }

    private void simulateCity() {
        output.printf("%n=== City simulation (%s mode) ===%n", buildMode.displayName());
        if (!readYesNo("Continue with this mode? (y/n): ")) {
            configureBuildMode();
        }

        BuildStrategy strategy = switch (buildMode) {
            case UNIFORM -> new UniformBuildStrategy(readBuildingPlan());
            case RANDOM -> new RandomBuildStrategy();
            case CENTRAL -> new CentralBuildStrategy();
        };

        CityBuildResult result = cityBuilder.build(grid, strategy);
        output.printf("Structures constructed: %d%n", result.structureCount());
        output.printf("Total construction cost: $%,.2f%n", result.totalCost());
        output.println(renderer.renderBuildMap(grid, result));

        if (result.placements().isEmpty()) {
            output.println("No structures satisfied the terrain and zoning constraints.");
            return;
        }

        output.println("Constructed structures:");
        result.placements().forEach(placement -> output.println("- " + placement));
    }

    private void configureBuildMode() {
        output.println("\n=== Build mode ===");
        output.println("1. Uniform — one plan for every cell");
        output.println("2. Random — independently generated plans");
        output.println("3. Central — taller, stronger structures near the centre");

        buildMode = switch (readIntInRange("Select a mode (1-3): ", 1, 3)) {
            case 1 -> BuildMode.UNIFORM;
            case 2 -> BuildMode.RANDOM;
            case 3 -> BuildMode.CENTRAL;
            default -> throw new IllegalStateException("Unreachable build-mode selection");
        };
        output.println("Build mode set to " + buildMode.displayName() + ".");
    }

    private BuildingPlan readBuildingPlan() {
        int floors = readIntInRange("Floors (1-1,000): ", 1, 1_000);
        FoundationType foundation = switch (readIntInRange(
                "Foundation — 1. slab, 2. stilts: ",
                1,
                2)) {
            case 1 -> FoundationType.SLAB;
            case 2 -> FoundationType.STILTS;
            default -> throw new IllegalStateException("Unreachable foundation selection");
        };
        MaterialType material = switch (readIntInRange(
                "Material — 1. wood, 2. stone, 3. brick, 4. concrete: ",
                1,
                4)) {
            case 1 -> MaterialType.WOOD;
            case 2 -> MaterialType.STONE;
            case 3 -> MaterialType.BRICK;
            case 4 -> MaterialType.CONCRETE;
            default -> throw new IllegalStateException("Unreachable material selection");
        };
        return new BuildingPlan(floors, foundation, material);
    }

    private int readIntInRange(String prompt, int minimum, int maximum) {
        while (true) {
            output.print(prompt);
            String value = input.nextLine().trim();
            try {
                int parsed = Integer.parseInt(value);
                if (parsed >= minimum && parsed <= maximum) {
                    return parsed;
                }
            } catch (NumberFormatException ignored) {
                // The user receives one consistent validation message below.
            }
            output.printf("Enter a whole number from %d to %d.%n", minimum, maximum);
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            output.print(prompt);
            String value = input.nextLine().trim();
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes")) {
                return true;
            }
            if (value.equalsIgnoreCase("n") || value.equalsIgnoreCase("no")) {
                return false;
            }
            output.println("Enter y or n.");
        }
    }
}
