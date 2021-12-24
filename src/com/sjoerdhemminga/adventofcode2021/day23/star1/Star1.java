package com.sjoerdhemminga.adventofcode2021.day23.star1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final Pattern REMOVE_POUND_CHAR = Pattern.compile("#");

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final String[] shrimps = lines.filter(not(String::isBlank))
                    .skip(2)
                    .map(String::strip)
                    .map(s -> REMOVE_POUND_CHAR.matcher(s)
                            .replaceAll(""))
                    .filter(not(String::isBlank))
                    .toArray(String[]::new);

            final Burrow start = Burrow.ofString(shrimps[0], shrimps[1]);

            final PriorityQueue<Burrow> queue = new PriorityQueue<>(Comparator.comparingInt(Burrow::expendedEnergy));
            queue.add(start);

            final Map<Burrow, Burrow> priorStates = new HashMap<>();

            int minEnergy = Integer.MAX_VALUE;

            while (!queue.isEmpty()) {
                final Burrow currState = queue.remove();

                if (currState.expendedEnergy() > minEnergy) continue;

                if (currState.expendedEnergy() % 1000 == 0) System.out.printf(
                        "Queue size = %6d   Prior states = %6d   Curr energy: %d%n",
                        queue.size(),
                        priorStates.size(),
                        currState.expendedEnergy());

                for (final Burrow nextState : currState.nextStates()) {
                    if (priorStates.containsKey(nextState.withoutEnergy()))
                        if (priorStates.get(nextState.withoutEnergy()).expendedEnergy() <= nextState.expendedEnergy())
                            continue; // Been there, done that, got the results. They're in the p-queue somewhere.

                    priorStates.put(nextState.withoutEnergy(), nextState);

                    if (nextState.inEndState()) {
                        System.out.println(nextState);
                        minEnergy = Math.min(minEnergy, nextState.expendedEnergy());
                    } else if (nextState.expendedEnergy() < minEnergy) {
                        queue.add(nextState);
                    }
                }
            }

            System.out.println("minEnergy = " + minEnergy);
        }
    }
}
