package com.sjoerdhemminga.adventofcode2021.day20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    private static final int STEPS = 50;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Spliterator<String> spliterator = lines
                    .filter(not(String::isBlank))
                    .spliterator();

            final Image inputImage = new Image();
            final EnhancementAlgorithm algorithm = new EnhancementAlgorithm();

            spliterator.tryAdvance(line -> algorithm.setAlgorithm(line.toCharArray()));
            spliterator.forEachRemaining(line -> inputImage.addLine(line.toCharArray()));

            Image currImage = inputImage;
            System.out.println(currImage);

            for (int step = 0; step < STEPS; step++) {
                currImage = algorithm.enhance(currImage);
            }

            System.out.println(currImage);
        }
    }
}
