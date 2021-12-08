package com.sjoerdhemminga.adventofcode2021.day08;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final long sum = lines.filter(not(String::isBlank))
                    .map(Signal::fromLine)
                    .peek(System.out::println)
                    .mapToLong(Signal::getValue)
                    .sum();

            System.out.println("sum = " + sum);
        }
    }
}
