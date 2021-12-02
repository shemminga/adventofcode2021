package com.sjoerdhemminga.adventofcode2021.day02;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<String[]> commands = lines.filter(not(String::isBlank))
                    .map(x -> x.split(" "))
                    .toList();

            int aim = 0;
            int depth = 0;
            int hPos = 0;
            for (final String[] cmd : commands) {
                final int value = Integer.parseInt(cmd[1]);
                switch (cmd[0]) {
                case "forward" -> {
                    hPos += value;
                    depth += aim * value;
                }
                case "up" -> aim -= value;
                case "down" -> aim += value;
                default -> throw new AssertionError();
                }
            }

            System.out.println("aim = " + aim);
            System.out.println("depth = " + depth);
            System.out.println("hPos = " + hPos);
            System.out.println("(depth * hPos) = " + (depth * hPos));
        }
    }
}
