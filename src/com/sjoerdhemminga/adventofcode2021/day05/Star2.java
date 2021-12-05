package com.sjoerdhemminga.adventofcode2021.day05;

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
            final Grid grid = new Grid();
            lines.filter(not(String::isBlank))
                    .map(Line::fromString)
                    .forEach(grid::addLine);

            System.out.println("grid = \n" + grid);

            final int count = grid.countMinValue(2);
            System.out.println("count = " + count);
        }
    }
}
