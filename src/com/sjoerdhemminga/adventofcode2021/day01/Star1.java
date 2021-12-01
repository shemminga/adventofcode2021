package com.sjoerdhemminga.adventofcode2021.day01;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] ints = lines.filter(Predicate.not(String::isBlank))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int cnt = 0;
            for (int i = 1; i < ints.length; i++)
                if (ints[i - 1] < ints[i]) cnt++;

            System.out.println(cnt);
        }
    }
}
