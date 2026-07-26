package io.github.himathahangama.urbangrid.application;

/**
 * City-wide building algorithms exposed by the console application.
 */
public enum BuildMode {
    UNIFORM("uniform"),
    RANDOM("random"),
    CENTRAL("central");

    private final String displayName;

    BuildMode(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
