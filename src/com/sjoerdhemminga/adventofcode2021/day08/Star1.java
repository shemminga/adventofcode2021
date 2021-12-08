package com.sjoerdhemminga.adventofcode2021.day08;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final long count = lines.filter(not(String::isBlank))
                    .map(s -> s.substring(s.indexOf('|') + 1))
                    .flatMap(s -> Arrays.stream(s.split(" ")))
                    .mapToInt(String::length)
                    .filter(n -> n == 2 || n == 4 || n == 3 || n == 7)
                    .count();

            System.out.println("count = " + count);
        }
    }
}
