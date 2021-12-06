package com.sjoerdhemminga.adventofcode2021.day06;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final int DAYS = 80;
    private static final int MAX_FISHES_TO_PRINT = 30;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            int[] fishes = lines.filter(not(String::isBlank))
                    .map(String::strip)
                    .flatMap(s -> Arrays.stream(s.split(",")))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            System.out.printf("Initial state: %4d %s%n", fishes.length, Arrays.toString(fishes));

            for (int day = 1; day <= DAYS; day++) {
                int newFishes = 0;

                for (int i = 0; i < fishes.length; i++) {
                    if (fishes[i] == 0) {
                        fishes[i] = 6;
                        newFishes++;
                    } else fishes[i]--;
                }

                final int[] fishesToday = new int[fishes.length + newFishes];
                Arrays.fill(fishesToday, 8);
                System.arraycopy(fishes, 0, fishesToday, 0, fishes.length);
                fishes = fishesToday;

                System.out.printf("After %2d days: %4d %s%n", day, fishes.length,
                        fishes.length < MAX_FISHES_TO_PRINT ? Arrays.toString(fishes) : "");
            }
        }
    }
}
