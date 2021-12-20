package com.sjoerdhemminga.adventofcode2021.day19;

record Coord(int x, int y, int z) {
    static final Coord ORIGIN = new Coord(0, 0, 0);

    Coord subtract(final Coord op2) {
        return new Coord(x - op2.x, y - op2.y, z - op2.z);
    }
}
