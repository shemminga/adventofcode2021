package com.sjoerdhemminga.adventofcode2021.day15;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

record Coord(int x, int y) {
    private Optional<Coord> getLeft() {
        return x <= 0 ? Optional.empty() : Optional.of(new Coord(x - 1, y));
    }

    private Optional<Coord> getTop() {
        return y <= 0 ? Optional.empty() : Optional.of(new Coord(x, y - 1));
    }

    private Optional<Coord> getRight(final Coord max) {
        return x >= max.x() ? Optional.empty() : Optional.of(new Coord(x + 1, y));
    }

    private Optional<Coord> getBottom(final Coord max) {
        return y >= max.y() ? Optional.empty() : Optional.of(new Coord(x, y + 1));
    }

    List<Coord> getNeighbours(final Coord max) {
        final List<Coord> neighbours = new ArrayList<>(4);
        getLeft().ifPresent(neighbours::add);
        getTop().ifPresent(neighbours::add);
        getRight(max).ifPresent(neighbours::add);
        getBottom(max).ifPresent(neighbours::add);
        return neighbours;
    }
}
