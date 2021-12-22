package com.sjoerdhemminga.adventofcode2021.day21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] positions = lines.filter(not(String::isBlank))
                    .map(s -> s.split(":"))
                    .map(a -> a[1])
                    .map(String::strip)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final int[] points = new int[positions.length];
            int movingPlayer = 0;
            int die = 1;
            int dieRolls = 0;

            while (points[0] < 1000 && points[1] < 1000) {
                die = playHalfRound(die, movingPlayer, positions, points);
                movingPlayer = movingPlayer == 0 ? 1 : 0;
                dieRolls += 3;
            }

            final int losingPlayerPoints;
            if (points[0] >= 1000 && points[1] < 1000) losingPlayerPoints = points[1];
            else if (points[0] < 1000 && points[1] >= 1000) losingPlayerPoints = points[0];
            else throw new AssertionError();

            System.out.printf("dieRolls: %d losingPlayerPoints: %d Product: %d%n", dieRolls, losingPlayerPoints, dieRolls * losingPlayerPoints);
        }
    }

    private static int playHalfRound(final int die, final int movingPlayer, final int[] positions, final int[] points) {
        int newDie = die;
        final int spaces = newDie++ + newDie++ + newDie++;

        positions[movingPlayer] += spaces;
        while (positions[movingPlayer] > 10) positions[movingPlayer] -= 10;

        points[movingPlayer] += positions[movingPlayer];

        return newDie;
    }
}
