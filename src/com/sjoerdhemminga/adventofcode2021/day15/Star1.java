package com.sjoerdhemminga.adventofcode2021.day15;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[][] grid = lines.filter(not(String::isBlank))
                    .map(s -> s.chars()
                            .map(c -> c - '0')
                            .toArray())
                    .toArray(int[][]::new);

            final long[][] distances = new long[grid.length][grid[0].length];
            for (final long[] row : distances) Arrays.fill(row, Long.MAX_VALUE);

            distances[0][0] = 0; // Start risk is ignored, because not entered.

            final Coord start = new Coord(0, 0);
            final Coord end = new Coord(grid[0].length - 1, grid.length - 1);

            final PriorityQueue<Coord> coords = new PriorityQueue<>(new DistanceCoordComparator(distances));
            coords.add(start);

            dijkstraBabyHurryDownTheCavernTonight(grid, distances, coords, end);

            System.out.println("distances[end.y()][end.x()] = " + distances[end.y()][end.x()]);
        }
    }

    private static void dijkstraBabyHurryDownTheCavernTonight(final int[][] grid, final long[][] distances,
            final PriorityQueue<Coord> coords, final Coord end) {

        while (!coords.isEmpty()) {
            final Coord c = coords.poll();

            c.getNeighbours(end)
                    .stream()
                    .filter(n -> distances[c.y()][c.x()] + grid[n.y()][n.x()] < distances[n.y()][n.x()])
                    .forEach(n -> {
                        distances[n.y()][n.x()] = distances[c.y()][c.x()] + grid[n.y()][n.x()];
                        coords.remove(n);
                        coords.add(n);
                    });
        }
    }
}
