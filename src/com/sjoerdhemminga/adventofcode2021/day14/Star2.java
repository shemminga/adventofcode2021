package com.sjoerdhemminga.adventofcode2021.day14;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Spliterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    private static final Pattern REACTION_SPLIT_PATTERN = Pattern.compile(" -> ");
    private static final int STEPS = 40;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Spliterator<String> spliterator = lines
                    .filter(not(String::isBlank))
                    .spliterator();

            final PairReactor reactor = new PairReactor();

            spliterator.tryAdvance(line -> {
                reactor.setFirstChar(line.charAt(0));
                for (int i = 1; i < line.length(); i++) reactor.addPair(line.substring(i - 1, i + 1));
            });

            spliterator.forEachRemaining(line -> {
                final String[] split = REACTION_SPLIT_PATTERN.split(line);
                reactor.addReaction(split[0], split[1].charAt(0));
            });

            System.out.printf("Template    : %s%n", reactor.getHistogram());

            for (int i = 1; i <= STEPS; i++) {
                reactor.react();
                System.out.printf("After step %2d: %s%n", i, reactor.getHistogram());
            }

            final Map<Character, Long> histogram = reactor.getHistogram();
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
