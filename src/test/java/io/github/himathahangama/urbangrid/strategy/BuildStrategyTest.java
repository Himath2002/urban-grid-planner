package io.github.himathahangama.urbangrid.strategy;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.FoundationType;
import io.github.himathahangama.urbangrid.domain.MaterialType;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildStrategyTest {
    @Test
    void uniformStrategyReturnsTheConfiguredPlan() {
        BuildingPlan plan = new BuildingPlan(
                3,
                FoundationType.STILTS,
                MaterialType.BRICK);

        assertEquals(plan, new UniformBuildStrategy(plan).generate(8, 5, 10, 10));
    }

    @Test
    void centralStrategyBuildsTallerAndStrongerNearTheCentre() {
        CentralBuildStrategy strategy = new CentralBuildStrategy();

        BuildingPlan centre = strategy.generate(5, 5, 11, 11);
        BuildingPlan edge = strategy.generate(0, 0, 11, 11);

        assertTrue(centre.floors() > edge.floors());
        assertEquals(MaterialType.CONCRETE, centre.material());
        assertNotEquals(MaterialType.CONCRETE, edge.material());
    }

    @Test
    void injectedRandomGeneratorMakesPlanningRepeatable() {
        RandomBuildStrategy first = new RandomBuildStrategy(new Random(42));
        RandomBuildStrategy second = new RandomBuildStrategy(new Random(42));

        for (int index = 0; index < 10; index++) {
            assertEquals(
                    first.generate(index, 0, 10, 1),
                    second.generate(index, 0, 10, 1));
        }
    }
}
