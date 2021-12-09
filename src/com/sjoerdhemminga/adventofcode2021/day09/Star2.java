package com.sjoerdhemminga.adventofcode2021.day09;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] grid = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            final long product = IntStream.range(0, grid.length)
                    .mapToObj(i -> IntStream.range(0, grid[i].length)
                            .filter(j -> j == 0 || grid[i][j - 1] > grid[i][j])
                            .filter(j -> j == grid[i].length - 1 || grid[i][j + 1] > grid[i][j])
                            .filter(j -> i == 0 || grid[i - 1][j] > grid[i][j])
                            .filter(j -> i == grid.length - 1 || grid[i + 1][j] > grid[i][j])
                            .mapToObj(j -> entry(i, j)))
                    .flatMap(stream -> stream)
                    .mapToLong(e -> floodFill(grid, e))
                    .map(n -> -n)
                    .sorted()
                    .map(n -> -n)
                    .limit(3)
                    .reduce((left, right) -> left * right)
                    .orElseThrow();

            System.out.println("product = " + product);
        }
    }

    private static int floodFill(final char[][] grid, final Entry<Integer, Integer> e) {
        final Deque<Entry<Integer, Integer>> toVisit = new ArrayDeque<>();
        final boolean[][] visited = new boolean[grid.length][grid[0].length];
        toVisit.push(e);

        int size = 0;
        while (!toVisit.isEmpty()) {
            final Entry<Integer, Integer> point = toVisit.pop();
            final int i = point.getKey();
            final int j = point.getValue();

            if (i < 0) continue;
            if (i >= grid.length) continue;
            if (j < 0) continue;
            if (j >= grid[0].length) continue;
            if (visited[i][j]) continue;
            if (grid[i][j] == '9') continue;

            size++;
            visited[i][j] = true;

            toVisit.push(entry(i - 1, j));
            toVisit.push(entry(i + 1, j));
            toVisit.push(entry(i, j - 1));
            toVisit.push(entry(i, j + 1));
        }

        return size;
    }

}
