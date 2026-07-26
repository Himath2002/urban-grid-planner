package io.github.himathahangama.urbangrid.strategy;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.FoundationType;
import io.github.himathahangama.urbangrid.domain.MaterialType;

import java.util.Objects;
import java.util.random.RandomGenerator;

public final class RandomBuildStrategy implements BuildStrategy {
    private static final int MAXIMUM_FLOORS = 5;

    private final RandomGenerator random;

    public RandomBuildStrategy() {
        this(RandomGenerator.getDefault());
    }

    public RandomBuildStrategy(RandomGenerator random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public BuildingPlan generate(int row, int column, int rows, int columns) {
        int floors = random.nextInt(1, MAXIMUM_FLOORS + 1);
        FoundationType[] foundations = FoundationType.values();
        MaterialType[] materials = MaterialType.values();

        return new BuildingPlan(
                floors,
                foundations[random.nextInt(foundations.length)],
                materials[random.nextInt(materials.length)]);
    }
}
