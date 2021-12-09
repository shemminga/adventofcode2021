package com.sjoerdhemminga.adventofcode2021.day09;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] numbers = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            final int totalRiskLevel = IntStream.range(0, numbers.length)
                    .flatMap(i -> IntStream.range(0, numbers[i].length)
                            .filter(j -> j == 0 || numbers[i][j - 1] > numbers[i][j])
                            .filter(j -> j == numbers[i].length - 1 || numbers[i][j + 1] > numbers[i][j])
                            .filter(j -> i == 0 || numbers[i - 1][j] > numbers[i][j])
                            .filter(j -> i == numbers.length - 1 || numbers[i + 1][j] > numbers[i][j])
                            .map(j -> numbers[i][j] - '0')
                            .map(n -> n + 1))
                    .sum();

            System.out.println("totalRiskLevel = " + totalRiskLevel);
        }
    }
}
