package com.sjoerdhemminga.adventofcode2021.day24;

record Range(long minValue, long maxValue) {
    static final Range ZERO = new Range(0, 0);

    Range {
        if (minValue > maxValue) throw new IllegalArgumentException();
    }

    boolean isSingleValue() {
        return minValue == maxValue;
    }

    boolean intersectsWith(final Range other) {
        return minValue <= other.maxValue && maxValue >= other.minValue;
    }

    boolean isDisjointWith(final Range other) {
        return !intersectsWith(other);
    }
}
