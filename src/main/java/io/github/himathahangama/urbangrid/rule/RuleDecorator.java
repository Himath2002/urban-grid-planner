package io.github.himathahangama.urbangrid.rule;

import java.util.Objects;

/**
 * Delegating base for dynamic zoning-rule composition.
 */
public abstract class RuleDecorator implements ZoningRule {
    protected final ZoningRule innerRule;

    protected RuleDecorator(ZoningRule innerRule) {
        this.innerRule = Objects.requireNonNull(innerRule, "innerRule");
    }

    public ZoningRule innerRule() {
        return innerRule;
    }

    @Override
    public double costMultiplier() {
        return innerRule.costMultiplier();
    }

    @Override
    public String code() {
        return innerRule.code();
    }

    @Override
    public String description() {
        return innerRule.description();
    }

    @Override
    public String warning() {
        return innerRule.warning();
    }

    @Override
    public boolean contaminated() {
        return innerRule.contaminated();
    }

    protected static String append(String existing, String value, String delimiter) {
        if (existing == null || existing.isBlank()) {
            return value;
        }
        return existing + delimiter + value;
    }
}
