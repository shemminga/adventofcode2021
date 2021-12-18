package com.sjoerdhemminga.adventofcode2021.day18;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final SnailfishNumber[] snailfishNumbers = lines.filter(not(String::isBlank))
                    .map(Parser::parse)
                    .toArray(SnailfishNumber[]::new);

            SnailfishNumber curr = null;
            for (final SnailfishNumber next : snailfishNumbers)
                if (curr == null) curr = next;
                else curr = Reducer.reduce(SnailfishNumber.add(curr, next));

            System.out.println("curr = " + curr);
            System.out.println("curr.magnitude() = " + curr.magnitude());
        }
    }
}
