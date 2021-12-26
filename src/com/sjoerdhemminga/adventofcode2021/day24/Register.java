package com.sjoerdhemminga.adventofcode2021.day24;

enum Register {
    W, X, Y, Z;

    static Register ofString(final String s) {
        return valueOf(s.toUpperCase());
    }
}
