package com.sjoerdhemminga.adventofcode2021.day25;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final char EAST = '>';
    private static final char SOUTH = 'v';

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] grid = lines.filter(not(String::isBlank))
                    .map(String::strip)
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            final Set<Coord> eastHurd = new HashSet<>();
            final Set<Coord> southHurd = new HashSet<>();
            final Coord max = new Coord(grid.length - 1, grid[0].length - 1);

            for (int x = 0; x < grid.length; x++)
                for (int y = 0; y < grid[x].length; y++)
                    if (grid[x][y] == EAST) eastHurd.add(new Coord(x, y));
                    else if (grid[x][y] == SOUTH) southHurd.add(new Coord(x, y));

            Round round = new Round(0, eastHurd, southHurd, true);
            while (round.hasMoved()) round = moveOneRound(round, max);

            System.out.println("round.number = " + round.number());
        }
    }

    private static Round moveOneRound(final Round round, final Coord max) {
        final Set<Coord> newEastHurd = new HashSet<>();
        final Set<Coord> newSouthHurd = new HashSet<>();

        for (final Coord c : round.eastHurd())
            if (round.eastHurd().contains(c.east(max)) || round.southHurd().contains(c.east(max))) newEastHurd.add(c);
            else newEastHurd.add(c.east(max));

        for (final Coord c : round.southHurd())
            if (newEastHurd.contains(c.south(max)) || round.southHurd().contains(c.south(max))) newSouthHurd.add(c);
            else newSouthHurd.add(c.south(max));

        return new Round(round.number() + 1,
                newEastHurd,
                newSouthHurd,
                !round.eastHurd().equals(newEastHurd) || !round.southHurd().equals(newSouthHurd));
    }
}
