package com.sjoerdhemminga.adventofcode2021.day19;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
interface OrientationMapper extends Function<Coord, Coord> {
    List<OrientationMapper> MAPPERS = List.of(
            c -> new Coord(c.x(), c.y(), c.z()),
            c -> new Coord(c.x(), -c.y(), -c.z()),
            c -> new Coord(c.x(), c.z(), -c.y()),
            c -> new Coord(c.x(), -c.z(), c.y()),

            c -> new Coord(-c.x(), c.y(), -c.z()),
            c -> new Coord(-c.x(), -c.y(), c.z()),
            c -> new Coord(-c.x(), c.z(), c.y()),
            c -> new Coord(-c.x(), -c.z(), -c.y()),

            c -> new Coord(c.y(), c.x(), -c.z()),
            c -> new Coord(c.y(), -c.x(), c.z()),
            c -> new Coord(c.y(), c.z(), c.x()),
            c -> new Coord(c.y(), -c.z(), -c.x()),

            c -> new Coord(-c.y(), c.x(), c.z()),
            c -> new Coord(-c.y(), -c.x(), -c.z()),
            c -> new Coord(-c.y(), c.z(), -c.x()),
            c -> new Coord(-c.y(), -c.z(), c.x()),

            c -> new Coord(c.z(), c.x(), c.y()),
            c -> new Coord(c.z(), -c.x(), -c.y()),
            c -> new Coord(c.z(), c.y(), -c.x()),
            c -> new Coord(c.z(), -c.y(), c.x()),

            c -> new Coord(-c.z(), c.x(), -c.y()),
            c -> new Coord(-c.z(), -c.x(), c.y()),
            c -> new Coord(-c.z(), c.y(), c.x()),
            c -> new Coord(-c.z(), -c.y(), -c.x()));
}
