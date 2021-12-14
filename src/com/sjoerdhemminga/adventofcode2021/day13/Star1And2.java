package com.sjoerdhemminga.adventofcode2021.day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1And2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1And2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Set<Point> origPoints = new HashSet<>();
            final List<Fold> folds = new ArrayList<>();

            lines.filter(not(String::isBlank))
                    .forEach(line -> {
                        if (line.startsWith("fold along ")) {
                            folds.add(Fold.fromString(line));
                        } else {
                            origPoints.add(Point.fromString(line));
                        }
                    });

            Set<Point> points = origPoints;
            System.out.println("Input points.size() = " + points.size());

            for (final Fold fold : folds) {
                points = fold(points, fold);
                System.out.printf("points.size() = %3d after fold %s%n", points.size(), fold);
            }

            printGrid(points);
        }
    }

    private static Set<Point> fold(final Set<Point> points, final Fold fold) {
        return points.stream()
                .map(fold)
                .collect(Collectors.toSet());
    }

    private static void printGrid(final Set<Point> points) {
        final Deque<Point> sortedPoints = points.stream()
                .sorted(Comparator.comparingInt(Point::y)
                        .thenComparingInt(Point::x))
                .collect(Collectors.toCollection(ArrayDeque::new));

        final int maxX = sortedPoints.stream()
                .mapToInt(Point::x)
                .max()
                .orElseThrow();

        final int maxY = sortedPoints.stream()
                .mapToInt(Point::y)
                .max()
                .orElseThrow();

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                //System.out.printf("x = %3d y = %3d peekFirst = %s%n", x, y, sortedPoints.peekFirst());
                if (new Point(x, y).equals(sortedPoints.peekFirst())) {
                    sortedPoints.removeFirst();
                    System.out.print('#');
                } else System.out.print('.');
            }
            System.out.println();
        }

        if (!sortedPoints.isEmpty()) throw new AssertionError();
    }
}
