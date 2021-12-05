package com.sjoerdhemminga.adventofcode2021.day05;

import java.util.regex.Pattern;

final class Line {
    private static final Pattern SPLIT_PATTERN = Pattern.compile(" -> ");
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private Line(final int x1, final int y1, final int x2, final int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    static Line fromString(final String s) {
        final String[] points = SPLIT_PATTERN.split(s);

        final String[] point1 = points[0].split(",");
        final String[] point2 = points[1].split(",");

        final int x1 = Integer.parseInt(point1[0]);
        final int y1 = Integer.parseInt(point1[1]);
        final int x2 = Integer.parseInt(point2[0]);
        final int y2 = Integer.parseInt(point2[1]);

        return new Line(x1, y1, x2, y2);
    }

    boolean isOrthogonal() {
        return isHorizontal() || isVertical();
    }

    boolean isHorizontal() {
        return y1 == y2;
    }

    boolean isVertical() {
        return x1 == x2;
    }

    boolean isOnLine(final int x, final int y) {
        boolean onLine = true;

        if (x1 < x2) onLine &= x >= x1 && x <= x2;
        else onLine &= x <= x1 && x >= x2;

        if (y1 < y2) onLine &= y >= y1 && y <= y2;
        else onLine &= y <= y1 && y >= y2;

        return onLine;
    }

    boolean isPoint() {
        return x1 == x2 && y1 == y2;
    }

    int getX1() {
        return x1;
    }

    int getY1() {
        return y1;
    }

    int getX2() {
        return x2;
    }

    int getY2() {
        return y2;
    }
}
