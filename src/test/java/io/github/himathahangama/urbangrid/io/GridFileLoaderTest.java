package io.github.himathahangama.urbangrid.io;

import io.github.himathahangama.urbangrid.domain.CityGrid;
import io.github.himathahangama.urbangrid.domain.TerrainType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GridFileLoaderTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void loadsTerrainAndComposedZoningRules() throws IOException {
        Path gridFile = writeGrid("composed-grid.txt", """
                2,2
                flat
                rocky,height-limit=4
                swampy,flood-risk=30,contamination
                flat,heritage=brick
                """);

        CityGrid grid = new GridFileLoader().load(gridFile);

        assertEquals(2, grid.rows());
        assertEquals(2, grid.columns());
        assertEquals(TerrainType.ROCKY, grid.cellAt(0, 1).terrain());
        assertEquals("F30 C", grid.cellAt(1, 0).zoningRule().code());
        assertEquals(2.4, grid.cellAt(1, 0).zoningRule().costMultiplier(), 0.000_001);
    }

    @Test
    void rejectsMissingAndUnexpectedRecords() throws IOException {
        Path missingRecord = writeGrid("missing-record.txt", """
                1,2
                flat
                """);
        Path extraRecord = writeGrid("extra-record.txt", """
                1,1
                flat
                rocky
                """);

        IllegalArgumentException missingException = assertThrows(
                IllegalArgumentException.class,
                () -> new GridFileLoader().load(missingRecord));
        IllegalArgumentException extraException = assertThrows(
                IllegalArgumentException.class,
                () -> new GridFileLoader().load(extraRecord));

        assertTrue(missingException.getMessage().contains("Missing record"));
        assertTrue(extraException.getMessage().contains("Unexpected data"));
    }

    private Path writeGrid(String fileName, String content) throws IOException {
        Path file = temporaryDirectory.resolve(fileName);
        Files.writeString(file, content);
        return file;
    }
}
