package com.sjoerdhemminga.adventofcode2021.day19;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sjoerdhemminga.adventofcode2021.day19.Coord.ORIGIN;
import static java.util.Map.entry;

final class ConstellationAligner {
    static Optional<Entry<Coord, List<Coord>>> align(final List<Coord> base, final List<Coord> unaligned) {
        return OrientationMapper.MAPPERS.stream()
                .map(m -> alignConstellation(base, unaligned, m))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    private static Optional<Entry<Coord, List<Coord>>> alignConstellation(final List<Coord> base,
            final List<Coord> unaligned, final OrientationMapper m) {
        final List<Coord> reorientedCoords = unaligned.stream()
                .map(m)
                .toList();

        final Map<Coord, Long> repositionCounts = base.stream()
                .flatMap(item -> reorientedCoords.stream()
                        .map(coord -> calcReposition(item, coord)))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return repositionCounts.entrySet()
                .stream()
                .filter(e -> e.getValue() >= 12)
                .map(Entry::getKey)
                .findAny()
                .map(reposition -> entry(ORIGIN.subtract(reposition), reorientedCoords.stream()
                        .map(c -> c.subtract(reposition))
                        .toList()));
    }

    private static Coord calcReposition(final Coord baseCoord, final Coord testeeCoord) {
        return testeeCoord.subtract(baseCoord);
    }
}
