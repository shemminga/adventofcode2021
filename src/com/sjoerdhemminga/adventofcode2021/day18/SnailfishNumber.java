package com.sjoerdhemminga.adventofcode2021.day18;

record SnailfishNumber(boolean isPair, int regularNumber, SnailfishNumber left, SnailfishNumber right) {
    static SnailfishNumber ofRegularNumber(final int n) {
        return new SnailfishNumber(false, n, null, null);
    }

    static SnailfishNumber ofPair(final SnailfishNumber left, final SnailfishNumber right) {
        return new SnailfishNumber(true, 0, left, right);
    }

    static SnailfishNumber add(final SnailfishNumber operandLeft, final SnailfishNumber operandRight) {
        return ofPair(operandLeft, operandRight);
    }

    int magnitude() {
        return isPair ? (3 * left.magnitude() + 2 * right.magnitude()) : regularNumber;
    }

    @Override
    public String toString() {
        return isPair ? ("[" + left + ',' + right + ']') : String.valueOf(regularNumber);
    }
}
