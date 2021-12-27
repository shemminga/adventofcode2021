package com.sjoerdhemminga.adventofcode2021.day25;

import java.util.Arrays;
import java.util.Set;

record Round(int number, Set<Coord> eastHurd, Set<Coord> southHurd, boolean hasMoved) {
    String toString(final Coord max) {
        final char[][] grid = new char[max.x() + 1][max.y() + 1];

        for (final char[] row : grid) Arrays.fill(row, '.');
        for (final Coord c : eastHurd) grid[c.x()][c.y()] = '>';
        for (final Coord c : southHurd) grid[c.x()][c.y()] = 'v';

        final StringBuilder sb = new StringBuilder();
        sb.append("Round: ").append(number).append(" hasMoved: ").append(hasMoved).append('\n');
        for (final char[] row : grid) sb.append(row).append('\n');
        return sb.toString();
    }
}
