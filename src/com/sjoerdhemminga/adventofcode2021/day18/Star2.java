package com.sjoerdhemminga.adventofcode2021.day18;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final SnailfishNumber[] snailfishNumbers = lines.filter(not(String::isBlank))
                    .map(Parser::parse)
                    .toArray(SnailfishNumber[]::new);

            final int maxMagnitude = IntStream.range(0, snailfishNumbers.length)
                    .mapToObj(i -> IntStream.range(0, snailfishNumbers.length)
                            .mapToObj(j -> new int[]{i, j}))
                    .flatMap(s -> s)
                    .filter(a -> a[0] != a[1])
                    .map(a -> SnailfishNumber.add(snailfishNumbers[a[0]], snailfishNumbers[a[1]]))
                    .map(Reducer::reduce)
                    .mapToInt(SnailfishNumber::magnitude)
                    .max()
                    .orElseThrow();

            System.out.println("maxMagnitude = " + maxMagnitude);
        }
    }
}
