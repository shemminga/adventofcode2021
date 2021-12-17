package com.sjoerdhemminga.adventofcode2021.day15;

import java.util.Comparator;

public final class DistanceCoordComparator implements Comparator<Coord> {
    private final long[][] distances;

    DistanceCoordComparator(final long[][] distances) {
        this.distances = distances;
    }

    @Override
    public int compare(final Coord o1, final Coord o2) {
        return Long.compare(distances[o1.y()][o1.x()], distances[o2.y()][o2.x()]);
    }
}
