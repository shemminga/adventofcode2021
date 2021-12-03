package com.sjoerdhemminga.adventofcode2021.day03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] counts = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .reduce(null,
                            (ints, chars) -> {
                                if (ints == null) ints = new int[chars.length];
                                for (int i = 0; i < chars.length; i++)
                                    ints[i] += chars[i] == '0' ? -1 : 1;
                                return ints;
                            },
                            (ints1, ints2) -> {
                                for (int i = 0; i < ints1.length; i++)
                                    ints1[i] += ints2[i];
                                return ints1;
                            });

            System.out.println("Arrays.toString(counts) = " + Arrays.toString(counts));

            final String gammaStr = Arrays.stream(counts)
                    .mapToObj(n -> n < 0 ? "0" : "1")
                    .collect(Collectors.joining());
            final String epsilonStr = Arrays.stream(counts)
                    .mapToObj(n -> n > 0 ? "0" : "1")
                    .collect(Collectors.joining());

            System.out.println("gammaStr = " + gammaStr);
            System.out.println("epsilonStr = " + epsilonStr);

            final int gamma = Integer.parseInt(gammaStr, 2);
            final int epsilon = Integer.parseInt(epsilonStr, 2);

            System.out.println("gamma = " + gamma);
            System.out.println("epsilon = " + epsilon);

            final int powerConsumption = gamma * epsilon;
            System.out.println("powerConsumption = " + powerConsumption);
        }
    }
}
