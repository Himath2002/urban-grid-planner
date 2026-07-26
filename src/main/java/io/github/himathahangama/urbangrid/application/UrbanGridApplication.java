package io.github.himathahangama.urbangrid.application;

import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.io.GridFileLoader;
import io.github.himathahangama.urbangrid.presentation.GridRenderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command-line entry point for UrbanGrid Planner.
 */
public final class UrbanGridApplication {
    private static final Logger LOGGER = Logger.getLogger(UrbanGridApplication.class.getName());

    private UrbanGridApplication() {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: ./gradlew run --args='<grid-file>'");
            return;
        }

        try {
            CityGrid grid = new GridFileLoader().load(Path.of(args[0]));
            GridRenderer renderer = new GridRenderer();

            System.out.printf(
                    "Grid loaded successfully (%d columns x %d rows).%n%n",
                    grid.columns(),
                    grid.rows());
            System.out.println(renderer.renderTerrainAndZoning(grid));

            new ConsoleSession(grid, renderer, System.in, System.out).run();
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Unable to read the grid file", exception);
            System.err.println("Unable to read the grid file: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            LOGGER.log(Level.WARNING, "Invalid grid data", exception);
            System.err.println("Invalid grid data: " + exception.getMessage());
        }
    }
}
