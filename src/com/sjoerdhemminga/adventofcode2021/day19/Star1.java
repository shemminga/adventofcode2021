package com.sjoerdhemminga.adventofcode2021.day19;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<List<Coord>> unalignedConstellations = readConstellations(lines);
            final List<List<Coord>> alignedConstellations = alignConstellations(unalignedConstellations);
            final Set<Coord> mergedConstellation = alignedConstellations.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

            System.out.println("mergedConstellation.size() = " + mergedConstellation.size());
        }
    }

    private static List<List<Coord>> alignConstellations(final List<List<Coord>> unalignedConstellations) {
        final List<List<Coord>> alignedConstellations = new ArrayList<>();
        alignedConstellations.add(unalignedConstellations.remove(0));

        for (int i = 0; i < alignedConstellations.size(); i++) {
            final List<List<Coord>> done = new ArrayList<>();
            for (final List<Coord> unalignedConstellation : unalignedConstellations)
                ConstellationAligner.align(alignedConstellations.get(i), unalignedConstellation)
                        .ifPresent(e -> {
                            alignedConstellations.add(e.getValue());
                            done.add(unalignedConstellation);
                        });
            unalignedConstellations.removeAll(done);
        }

        if (!unalignedConstellations.isEmpty()) throw new AssertionError();

        return alignedConstellations;
    }

    private static List<List<Coord>> readConstellations(final Stream<String> lines) {
        final Iterator<String> iterator = lines
                .filter(not(String::isBlank))
                .iterator();

        final List<List<Coord>> unalignedConstellations = new ArrayList<>();
        List<Coord> currentConstellation = null;

        while(iterator.hasNext()) {
            final String line = iterator.next();

            if (line.startsWith("--- ")) {
                if (currentConstellation != null) unalignedConstellations.add(currentConstellation);
                currentConstellation = new ArrayList<>();
            } else {
                final String[] coords = line.split(",");
                final Coord coord = new Coord(parseInt(coords[0]), parseInt(coords[1]), parseInt(coords[2]));
                currentConstellation.add(coord);
            }
        }
        unalignedConstellations.add(currentConstellation);
        return unalignedConstellations;
    }
}
