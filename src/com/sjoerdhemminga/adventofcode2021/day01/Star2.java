package com.sjoerdhemminga.adventofcode2021.day01;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] ints = lines.filter(Predicate.not(String::isBlank))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final int[] slices = new int[ints.length - 2];
            for (int i = 0; i < ints.length - 2; i++)
                slices[i] = ints[i] + ints[i + 1] + ints[i + 2];

            int cnt = 0;
            for (int i = 1; i < slices.length; i++)
                if (slices[i - 1] < slices[i]) cnt++;

            System.out.println(cnt);
        }
    }
}
