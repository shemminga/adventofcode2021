package com.sjoerdhemminga.adventofcode2021.day21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] positions = lines.filter(not(String::isBlank))
                    .map(s -> s.split(":"))
                    .map(a -> a[1])
                    .map(String::strip)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final int[] rollProbs = getRollProbs();

            final Deque<BoardState> stack = new ArrayDeque<>();
            stack.push(new BoardState(0, 0, positions[0], positions[1], 1, 1));

            long player1Wins = 0;
            long player2Wins = 0;

            while (!stack.isEmpty()) {
                final BoardState pop = stack.pop();

                if (pop.movingPlayer() == 1)
                    for (int roll = 3; roll <= 9; roll++) {
                        final int nextPos = getNextPos(pop.pos1(), roll);
                        final int nextScore = pop.score1() + nextPos;
                        final long nextUniverses = rollProbs[roll] * pop.universes();
                        if (nextScore >= 21) player1Wins += nextUniverses;
                        else
                            stack.push(new BoardState(nextScore, pop.score2(), nextPos, pop.pos2(), 2, nextUniverses));
                    }
                else
                    for (int roll = 3; roll <= 9; roll++) {
                        final int nextPos = getNextPos(pop.pos2(), roll);
                        final int nextScore = pop.score2() + nextPos;
                        final long nextUniverses = rollProbs[roll] * pop.universes();
                        if (nextScore >= 21) player2Wins += nextUniverses;
                        else
                            stack.push(new BoardState(pop.score1(), nextScore, pop.pos1(), nextPos, 1, nextUniverses));
                    }
            }

            System.out.println("player1Wins = " + player1Wins);
            System.out.println("player2Wins = " + player2Wins);
        }
    }

    private static int[] getRollProbs() {
        final int[] rollProbs = new int[10];
        for (int throw1 = 1; throw1 <= 3; throw1++)
            for (int throw2 = 1; throw2 <= 3; throw2++)
                for (int throw3 = 1; throw3 <= 3; throw3++)
                    rollProbs[throw1 + throw2 + throw3]++;
        return rollProbs;
    }

    private static int getNextPos(final int movPos, final int roll) {
        int nextPos = movPos + roll;
        while (nextPos > 10) nextPos -= 10;
        return nextPos;
    }
}
