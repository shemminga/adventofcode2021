package com.sjoerdhemminga.adventofcode2021.day14;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Map;
import java.util.Spliterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final Pattern REACTION_SPLIT_PATTERN = Pattern.compile(" -> ");
    private static final int STEPS = 10;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Spliterator<String> spliterator = lines
                    .filter(not(String::isBlank))
                    .spliterator();

            final LinkedListReactor reactor = new LinkedListReactor();

            spliterator.tryAdvance(line -> reactor.setPolymer(line.chars()
                    .mapToObj(c -> (char)c)
                    .collect(Collectors.toCollection(LinkedList::new))));

            spliterator.forEachRemaining(line -> {
                final String[] split = REACTION_SPLIT_PATTERN.split(line);
                reactor.addReaction(split[0], split[1].charAt(0));
            });

            reactor.printlnPolymer("Template    : ");

            for (int i = 1; i <= STEPS; i++) {
                reactor.react();
                reactor.printlnPolymer(String.format("After step %2d:", i));
            }

            final Map<Character, Long> histogram = reactor.getPolymer()
                    .chars()
                    .mapToObj(c -> (char)c)
                    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

            System.out.println("histogram = " + histogram);

            final long max = histogram.values()
                    .stream()
                    .mapToLong(l -> l)
                    .max()
                    .orElseThrow();
            final long min = histogram.values()
                    .stream()
                    .mapToLong(l -> l)
                    .min()
                    .orElseThrow();

            System.out.printf("max %5d - min %5d = %5d%n", max, min, max - min);
        }
    }
}
