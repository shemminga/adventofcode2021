package com.sjoerdhemminga.adventofcode2021.day19;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2021.day19.Coord.ORIGIN;
import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<List<Coord>> unalignedConstellations = readConstellations(lines);
            final List<Coord> scanners = findScanners(unalignedConstellations);

            scanners.stream()
                    .flatMapToInt(scanner1 -> scanners.stream()
                            .mapToInt(scanner2 -> calcManhattanDistance(scanner1, scanner2)))
                    .sorted()
                    .forEach(System.out::println);
        }
    }

    private static int calcManhattanDistance(final Coord scanner1, final Coord scanner2) {
        return Math.abs(scanner1.x() - scanner2.x()) +
                Math.abs(scanner1.y() - scanner2.y()) +
                Math.abs(scanner1.z() - scanner2.z());
    }

    private static List<Coord> findScanners(final List<List<Coord>> unalignedConstellations) {
        final List<Coord> scanners = new ArrayList<>();
        scanners.add(ORIGIN); // Scanner 0

        final List<List<Coord>> alignedConstellations = new ArrayList<>();
        alignedConstellations.add(unalignedConstellations.remove(0));

        for (int i = 0; i < alignedConstellations.size(); i++) {
            final List<List<Coord>> done = new ArrayList<>();
            for (final List<Coord> unalignedConstellation : unalignedConstellations)
                ConstellationAligner.align(alignedConstellations.get(i), unalignedConstellation)
                        .ifPresent(e -> {
                            scanners.add(e.getKey());
                            alignedConstellations.add(e.getValue());
                            done.add(unalignedConstellation);
                        });
            unalignedConstellations.removeAll(done);
        }

        if (!unalignedConstellations.isEmpty()) throw new AssertionError();

        return scanners;
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
