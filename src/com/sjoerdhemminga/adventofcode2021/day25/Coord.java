package com.sjoerdhemminga.adventofcode2021.day25;

record Coord(int x, int y) {
    Coord east(final Coord max) {
        return new Coord(x, y == max.y ? 0 : y + 1);
    }

    Coord south(final Coord max) {
        return new Coord(x == max.x ? 0 : x + 1, y);
    }
}
