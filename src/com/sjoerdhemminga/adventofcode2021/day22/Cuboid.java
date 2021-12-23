package com.sjoerdhemminga.adventofcode2021.day22;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;

record Cuboid(int fromX, int toX, int fromY, int toY, int fromZ, int toZ) {
    long volume() {
        final long xSize = toX - fromX + 1;
        final long ySize = toY - fromY + 1;
        final long zSize = toZ - fromZ + 1;
        return xSize * ySize * zSize;
    }

    Set<Cuboid> split(final Cuboid other){
        if (!overlaps(other)) return Set.of(this);
        if (other.fullyOverlaps(this)) return Set.of();

        final List<Entry<Integer, Integer>> xSplits = determineChunks(fromX, toX, other.fromX, other.toX);
        final List<Entry<Integer, Integer>> ySplits = determineChunks(fromY, toY, other.fromY, other.toY);
        final List<Entry<Integer, Integer>> zSplits = determineChunks(fromZ, toZ, other.fromZ, other.toZ);

        final Set<Cuboid> cuboids = xSplits.stream()
                .flatMap(xSplit -> ySplits.stream()
                        .flatMap(ySplit -> zSplits.stream()
                                .map(zSplit -> new Cuboid(xSplit.getKey(), xSplit.getValue(), ySplit.getKey(),
                                        ySplit.getValue(), zSplit.getKey(), zSplit.getValue()))))
                .collect(Collectors.toUnmodifiableSet());

        // Sanity checks
        final long sumVolume = cuboids.stream()
                .mapToLong(Cuboid::volume)
                .sum();
        if (sumVolume != volume()) throw new AssertionError();

        final boolean noCrossing = cuboids.stream()
                .allMatch(c -> other.fullyOverlaps(c) || !other.overlaps(c));
        if (!noCrossing) throw new AssertionError();

        return cuboids;
    }

    private static List<Entry<Integer, Integer>> determineChunks(final int from1, final int to1, final int from2,
            final int to2) {
        if (from2 <= from1 && to2 >= to1) return List.of(entry(from1, to1));
        if (from2 <= from1 && to2 < to1) return List.of(entry(from1, to2), entry(to2 + 1, to1));
        if (from2 > from1 && to2 >= to1) return List.of(entry(from1, from2 - 1), entry(from2, to1));
        if (from2 > from1 && to2 < to1) return List.of(entry(from1, from2 - 1), entry(from2, to2), entry(to2 + 1, to1));

        throw new AssertionError();
    }

    boolean fullyOverlaps(final Cuboid other) {
        return fullyOverlaps(fromX, toX, other.fromX, other.toX) &&
                fullyOverlaps(fromY, toY, other.fromY, other.toY) &&
                fullyOverlaps(fromZ, toZ, other.fromZ, other.toZ);
    }

    boolean overlaps(final Cuboid other) {
        return overlaps(fromX, toX, other.fromX, other.toX) &&
                overlaps(fromY, toY, other.fromY, other.toY) &&
                overlaps(fromZ, toZ, other.fromZ, other.toZ);
    }

    private static boolean fullyOverlaps(final int from1, final int to1, final int from2, final int to2) {
        return between(from1, to1, from2) && between(from1, to1, to2);
    }

    private static boolean overlaps(final int from1, final int to1, final int from2, final int to2) {
        return between(from1, to1, from2) ||
                between(from1, to1, to2) ||
                between(from2, to2, from1) ||
                between(from2, to2, to1);
    }

    private static boolean between(final int from, final int to, final int point) {
        return point >= from && point <= to;
    }
}
