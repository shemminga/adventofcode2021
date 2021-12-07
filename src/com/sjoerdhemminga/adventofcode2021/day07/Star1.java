package com.sjoerdhemminga.adventofcode2021.day07;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] crabs = lines.filter(not(String::isBlank))
                    .map(String::strip)
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final int maxDepth = Arrays.stream(crabs)
                    .max()
                    .orElseThrow();

            final int[] totalDistances = new int[maxDepth + 1];

            for (final int crabDepth : crabs)
                for (int i = 0; i < totalDistances.length; i++)
                    totalDistances[i] += Math.abs(crabDepth - i);

            IntStream.range(0, totalDistances.length)
                    .mapToObj(i -> entry(i, totalDistances[i]))
                    .sorted(Comparator.comparingInt(Map.Entry::getValue))
                    .forEach(e -> System.out.printf("- Align on depth %4d Fuel use %6d%n", e.getKey(), e.getValue()));

        }
    }
}
