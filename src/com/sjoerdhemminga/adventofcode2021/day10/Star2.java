package com.sjoerdhemminga.adventofcode2021.day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final long[] completionScores = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .mapToLong(Star2::scoreLine)
                    .filter(n -> n != 0)
                    .sorted()
                    .toArray();

            System.out.println("Arrays.toString(completionScores) = " + Arrays.toString(completionScores));
            System.out.println("completionScores.length = " + completionScores.length);

            final int medianIdx = completionScores.length / 2;
            final long medianScore = completionScores[medianIdx];
            System.out.println("medianScore = " + medianScore);
        }
    }

    private static long scoreLine(final char[] line) {
        final Deque<Character> braceStack = new ArrayDeque<>();

        for (final char c : line) {
            if (isOpenBrace(c)) braceStack.push(c);
            else if (isCloseBrace(c)) {
                if (braceStack.isEmpty()) return 0;

                final char expected = matchOpenBrace(braceStack.pop());
                if (c != expected) return 0;
            } else throw new AssertionError();
        }

        if (braceStack.isEmpty()) throw new AssertionError();

        long score = 0;
        while (!braceStack.isEmpty()) {
            final char c = braceStack.pop();
            score *= 5L;
            score += scoreMissingMatchFor(c);
        }

        return score;
    }

    private static boolean isOpenBrace(final char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }

    private static boolean isCloseBrace(final char c) {
        return c == ')' || c == ']' || c == '}' || c == '>';
    }

    private static char matchOpenBrace(final char open) {
        return switch (open) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            case '<' -> '>';
            default -> throw new AssertionError();
        };
    }

    private static long scoreMissingMatchFor(final char c) {
        return switch (c) {
            case '(' -> 1;
            case '[' -> 2;
            case '{' -> 3;
            case '<' -> 4;
            default -> throw new AssertionError();
        };
    }
}
