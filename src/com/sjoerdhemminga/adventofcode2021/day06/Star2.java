package com.sjoerdhemminga.adventofcode2021.day06;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    private static final int DAYS = 256;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] fishes = lines.filter(not(String::isBlank))
                    .map(String::strip)
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final long[] counts = new long[9];
            for(final int fish : fishes) counts[fish]++;

            System.out.printf("Initial state: %4d = %4d%n", Arrays.stream(counts).sum(), fishes.length);

            for (int day = 1; day <= DAYS; day++) {
                final long newFishes = counts[0];

                for (int i = 1; i < counts.length; i++)
                    counts[i - 1] = counts[i];
                counts[6] += newFishes;
                counts[8] = newFishes;

                System.out.printf("After %2d days: %4d%n", day, Arrays.stream(counts).sum());
            }
        }
    }
}
