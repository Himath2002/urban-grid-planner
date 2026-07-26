package io.github.himathahangama.urbangrid.rule;

import io.github.himathahangama.urbangrid.domain.BuildingPlan;
import io.github.himathahangama.urbangrid.domain.FoundationType;
import io.github.himathahangama.urbangrid.domain.MaterialType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZoningRuleTest {
    @Test
    void composesIndependentRulesWithoutLosingInnerBehavior() {
        ZoningRule rule = new ContaminationRule(
                new FloodRiskRule(
                        25,
                        new HeightLimitRule(
                                4,
                                new HeritageRule(
                                        MaterialType.BRICK,
                                        new DefaultZoningRule()))));

        assertTrue(rule.allows(plan(3, MaterialType.BRICK)));
        assertFalse(rule.allows(plan(1, MaterialType.BRICK)));
        assertFalse(rule.allows(plan(3, MaterialType.WOOD)));
        assertFalse(rule.allows(plan(5, MaterialType.BRICK)));
        assertEquals(2.25, rule.costMultiplier());
        assertEquals("HB L4 F25 C", rule.code());
        assertTrue(rule.contaminated());
    }

    private BuildingPlan plan(int floors, MaterialType material) {
        return new BuildingPlan(floors, FoundationType.STILTS, material);
    }
}
