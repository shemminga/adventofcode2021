package com.sjoerdhemminga.adventofcode2021.day13;

record Point(int x, int y) {
    static Point fromString(final String line) {
        final String[] coords = line.split(",");

        return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }
}
