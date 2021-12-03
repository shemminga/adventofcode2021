package com.sjoerdhemminga.adventofcode2021.day03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<char[]> numbers = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .toList();

            final List<char[]> nrsForO2 = new ArrayList<>(numbers);
            final List<char[]> nrsForCO2 = new ArrayList<>(numbers);

            for (int i = 0; i < numbers.get(0).length; i++) {
                final int finalI = i;

                if (nrsForO2.size() > 1) {
                    final char most = countMostBits(nrsForO2, i) < 0 ? '0' : '1';
                    nrsForO2.removeIf(n -> n[finalI] != most);
                }

                if (nrsForCO2.size() > 1) {
                    final char least = countMostBits(nrsForCO2, i) < 0 ? '1' : '0';
                    nrsForCO2.removeIf(n -> n[finalI] != least);
                }
            }

            final String o2RatingBin = String.valueOf(nrsForO2.get(0));
            final String co2RatingBin = String.valueOf(nrsForCO2.get(0));

            System.out.println("o2RatingBin = " + o2RatingBin);
            System.out.println("co2RatingBin = " + co2RatingBin);

            final int o2Rating = Integer.parseInt(o2RatingBin, 2);
            final int co2Rating = Integer.parseInt(co2RatingBin, 2);

            System.out.println("o2Rating = " + o2Rating);
            System.out.println("co2Rating = " + co2Rating);

            final int lifeSupportRating = o2Rating * co2Rating;

            System.out.println("lifeSupportRating = " + lifeSupportRating);
        }
    }

    private static int countMostBits(final List<char[]> numbers, final int i) {
        return numbers.stream()
                .reduce(0,
                        (count, chars) -> count + (chars[i] == '0' ? -1 : 1),
                        Integer::sum);
    }
}
