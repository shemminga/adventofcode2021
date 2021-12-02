package com.sjoerdhemminga.adventofcode2021.day02;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<String[]> commands = lines.filter(not(String::isBlank))
                    .map(x -> x.split(" "))
                    .toList();

            final int totalForward = totalDirection(commands, "forward");
            final int totalDown = totalDirection(commands, "down");
            final int totalUp = totalDirection(commands, "up");
            final int depth = totalDown - totalUp;
            final int solution = totalForward * depth;

            System.out.println("totalForward = " + totalForward);
            System.out.println("totalDown = " + totalDown);
            System.out.println("totalUp = " + totalUp);
            System.out.println("depth = " + depth);
            System.out.println("solution = " + solution);
        }
    }

    private static int totalDirection(final List<String[]> commands, final String direction) {
        return commands.stream()
                .filter(cmd -> direction.equals(cmd[0]))
                .map(cmd -> cmd[1])
                .mapToInt(Integer::parseInt)
                .sum();
    }
}
