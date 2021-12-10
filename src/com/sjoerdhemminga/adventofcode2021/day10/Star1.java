package com.sjoerdhemminga.adventofcode2021.day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int syntaxErrorScore = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .mapToInt(Star1::scoreLine)
                    .sum();

            System.out.println("syntaxErrorScore = " + syntaxErrorScore);
        }
    }

    private static int scoreLine(final char[] line) {
        final Deque<Character> braceStack = new ArrayDeque<>();

        for (final char c : line) {
            if (isOpenBrace(c)) braceStack.push(c);
            else if (isCloseBrace(c)) {
                if (braceStack.isEmpty()) return scoreUnexpected(c);

                final char expected = matchOpenBrace(braceStack.pop());
                if (c != expected) return scoreUnexpected(c);
            } else throw new AssertionError();
        }

        if (!braceStack.isEmpty()) {
            return 0;
        }

        throw new AssertionError();
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

    private static int scoreUnexpected(final char c) {
        return switch (c) {
            case ')' -> 3;
            case ']' -> 57;
            case '}' -> 1197;
            case '>' -> 25137;
            default -> throw new AssertionError();
        };
    }
}
