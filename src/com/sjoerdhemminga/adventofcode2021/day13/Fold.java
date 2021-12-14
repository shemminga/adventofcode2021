package com.sjoerdhemminga.adventofcode2021.day13;

import java.util.function.Function;

@FunctionalInterface
interface Fold extends Function<Point, Point> {
    static Fold fromString(final String line) {
        if (line.startsWith("fold along y=")) {
            return new HorizontalFold(extractCoord(line));
        }
        if (line.startsWith("fold along x=")) {
            return new VerticalFold(extractCoord(line));
        }
        throw new AssertionError();
    }

    static int extractCoord(final String line) {
        return Integer.parseInt(line.substring(13));
    }

    static int foldCoord(final int coord, final int foldCoord) {
        if (coord < foldCoord) return coord;

        return foldCoord - (coord - foldCoord);
    }

    record VerticalFold(int foldX) implements Fold {
        @Override
        public Point apply(final Point p) {
            return new Point(foldCoord(p.x(), foldX), p.y());
        }
    }

    record HorizontalFold(int foldY) implements Fold {
        @Override
        public Point apply(final Point p) {
            return new Point(p.x(), foldCoord(p.y(), foldY));
        }
    }
}
