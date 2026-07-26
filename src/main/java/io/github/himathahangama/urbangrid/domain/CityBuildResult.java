package io.github.himathahangama.urbangrid.domain;

import java.util.List;

public record CityBuildResult(List<BuildPlacement> placements, double totalCost) {
    public CityBuildResult {
        placements = List.copyOf(placements);
        if (totalCost < 0) {
            throw new IllegalArgumentException("Total cost cannot be negative.");
        }
    }

    public int structureCount() {
        return placements.size();
    }

    public boolean containsPlacement(int row, int column) {
        return placements.stream()
                .anyMatch(placement -> placement.row() == row && placement.column() == column);
    }
}
