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

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[][] origGrid = lines.filter(not(String::isBlank))
                    .map(s -> s.chars()
                            .map(c -> c - '0')
                            .toArray())
                    .toArray(int[][]::new);

            //printGrid(origGrid);
            //System.out.println();

            final int[][] grid = growGrid(origGrid, 5);

            //printGrid(grid);
            //System.out.println();

            final long[][] distances = new long[grid.length][grid[0].length];
            for (final long[] row : distances) Arrays.fill(row, Long.MAX_VALUE);

            distances[0][0] = 0; // Start risk is ignored, because not entered.

            final Coord start = new Coord(0, 0);
            final Coord end = new Coord(grid[0].length - 1, grid.length - 1);

            final PriorityQueue<Coord> coords = new PriorityQueue<>(new DistanceCoordComparator(distances));
            coords.add(start);

            dijkstraBabyHurryDownTheCavernTonight(grid, distances, coords, end);

            //System.out.println();
            //printDistancesGrid(distances);

            System.out.println("distances[end.y()][end.x()] = " + distances[end.y()][end.x()]);
        }
    }

    private static int[][] growGrid(final int[][] grid, final int factor) {
        final int[][] bigGrid = new int[grid.length * factor][grid[0].length * factor];

        for (int fi = 0; fi < factor; fi++)
            for (int fj = 0; fj < factor; fj++)
                for (int i = 0; i < grid.length; i++)
                    for (int j = 0; j < grid[i].length; j++) {
                        int newValue = grid[i][j] + fi + fj;
                        while (newValue > 9) newValue -= 9;
                        bigGrid[i + fi * grid.length][j + fj * grid[i].length] = newValue;
                    }

        return bigGrid;
    }

    private static void dijkstraBabyHurryDownTheCavernTonight(final int[][] grid, final long[][] distances,
            final PriorityQueue<Coord> coords, final Coord end) {
        final Coord[][] route = new Coord[grid.length][grid[0].length];

        while (!coords.isEmpty()) {
            final Coord c = coords.poll();

            c.getNeighbours(end)
                    .stream()
                    .filter(n -> distances[c.y()][c.x()] + grid[n.y()][n.x()] < distances[n.y()][n.x()])
                    .forEach(n -> {
                        route[n.y()][n.x()] = c;
                        distances[n.y()][n.x()] = distances[c.y()][c.x()] + grid[n.y()][n.x()];
                        coords.remove(n);
                        coords.add(n);
                    });
        }

        doubleCheckRoute(route, distances, end);
        //printRouteGrid(route, end);
    }

    private static void doubleCheckRoute(final Coord[][] route, final long[][] distances, final Coord end) {
        Coord c = end;
        while (route[c.y()][c.x()] != null) {
            final Coord p = route[c.y()][c.x()];
            final long distP = distances[p.y()][p.x()];

            c.getNeighbours(end)
                    .stream()
                    .filter(n -> p != n)
                    .forEach(n -> {
                        final long distN = distances[n.y()][n.x()];

                        if (distN < distP) {
                            System.out.printf("WRONG!!! %d < %d (neighbour %s < previous %s)%n", distN,
                                    distP, n, p);
                        }
                    });

            c = p;
        }
    }

    private static void printGrid(final int[][] grid) {
        for (final int[] row : grid) {
            for (final int cell : row) System.out.print(cell);
            System.out.println();
        }
    }

    private static void printRouteGrid(final Coord[][] route, final Coord end) {
        final char[][] printGrid = new char[route.length][route[0].length];

        for (final char[] row : printGrid) Arrays.fill(row, '.');

        Coord c = end;
        while (route[c.y()][c.x()] != null) {
            printGrid[c.y()][c.x()] = '#';
            c = route[c.y()][c.x()];
        }

        for (final char[] row : printGrid) {
            for (final char cell : row) System.out.print(cell);
            System.out.println();
        }
    }

    private static void printDistancesGrid(final long[][] distances) {
        for (final long[] row : distances) {
            System.out.println(Arrays.toString(row));
        }
    }
}
