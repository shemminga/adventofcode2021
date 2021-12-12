package com.sjoerdhemminga.adventofcode2021.day11;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[][] grid = lines.filter(not(String::isBlank))
                    .map(s -> s.chars()
                            .map(c -> c - '0')
                            .toArray())
                    .toArray(int[][]::new);

            int newFlashes = 0;
            int step = 0;
            do {
                step++;
                newFlashes = flashStep(grid);
                System.out.printf("Step %3d: %4d flashes%n", step, newFlashes);
            } while (newFlashes < grid.length * grid[0].length);
        }
    }

    private static int flashStep(final int[][] grid) {
        int count = 0;
        final boolean[][] flashed = new boolean[grid.length][grid[0].length];

        int newFlashes;
        increaseEnergy(grid);
        do {
            newFlashes = flash(grid, flashed);
            count += newFlashes;
        } while (newFlashes > 0);

        resetFlashers(grid, flashed);
        return count;
    }

    private static void increaseEnergy(final int[][] grid) {
        IntStream.range(0, grid.length)
                .forEach(i -> IntStream.range(0, grid[0].length)
                        .forEach(j -> grid[i][j]++));
    }

    private static int flash(final int[][] grid, final boolean[][] flashed) {
        int flashCount = 0;

        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
                if (!flashed[i][j] && grid[i][j] > 9) {
                    flashCount++;
                    flashCell(grid, flashed, i, j);
                }

        return flashCount;
    }

    private static void flashCell(final int[][] grid, final boolean[][] flashed, final int i, final int j) {
        flashed[i][j] = true;

        if (i > 0) {
            if (j > 0) grid[i - 1][j - 1]++;
            grid[i - 1][j]++;
            if (j < grid[i].length - 1) grid[i - 1][j + 1]++;
        }

        if (j > 0) grid[i][j - 1]++;
        if (j < grid[i].length - 1) grid[i][j + 1]++;

        if (i < grid.length - 1) {
            if (j > 0) grid[i + 1][j - 1]++;
            grid[i + 1][j]++;
            if (j < grid[i].length - 1) grid[i + 1][j + 1]++;
        }
    }

    private static void resetFlashers(final int[][] grid, final boolean[][] flashed) {
        IntStream.range(0, grid.length)
                .forEach(i -> IntStream.range(0, grid[0].length)
                        .filter(j -> flashed[i][j])
                        .forEach(j -> grid[i][j] = 0));
    }

    private static void printGrid(final int[][] grid) {
        for (final int[] row : grid) {
            for (final int cell : row) System.out.print(cell);
            System.out.println();
        }
        System.out.println();
    }

    private static void printFlashed(final boolean[][] flashed) {
        for (final boolean[] row : flashed) {
            for (final boolean b : row) System.out.print(b ? '*' : '.');
            System.out.println();
        }
        System.out.println();
    }
}
