package com.sjoerdhemminga.adventofcode2021.day22;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

final class ReactorCore {
    private final Set<Cuboid> onRegions = new HashSet<>();

    void execute(final Instruction instruction) {
        switch (instruction.onOff()) {
        case ON -> switchOn(instruction.cuboid());
        case OFF -> switchOff(instruction.cuboid());
        }
    }

    private void switchOn(final Cuboid cuboid) {
        final boolean superfluous = onRegions.stream()
                .anyMatch(c -> c.fullyOverlaps(cuboid));

        if (superfluous) return;

        final Optional<Cuboid> overlappingCuboid = onRegions.stream()
                .filter(c -> c.overlaps(cuboid))
                .findAny();

        overlappingCuboid.ifPresentOrElse(
                c -> cuboid.split(c).forEach(this::switchOn),
                () -> onRegions.add(cuboid));
    }

    private void switchOff(final Cuboid cuboid) {
        final Set<Cuboid> cuboidsToSplit = onRegions.stream()
                .filter(not(cuboid::fullyOverlaps))
                .filter(cuboid::overlaps)
                .collect(Collectors.toSet());

        final Set<Cuboid> splitCuboids = cuboidsToSplit.stream()
                .flatMap(c -> c.split(cuboid)
                        .stream())
                .collect(Collectors.toSet());

        onRegions.removeAll(cuboidsToSplit);
        onRegions.addAll(splitCuboids);

        final Set<Cuboid> cuboidsToSwitchOff = onRegions.stream()
                .filter(cuboid::fullyOverlaps)
                .collect(Collectors.toSet());

        onRegions.removeAll(cuboidsToSwitchOff);
    }

    long cubesSwitchedOn() {
        return onRegions.stream()
                .mapToLong(Cuboid::volume)
                .sum();
    }

    @Override
    public String toString() {
        return "regions=" + onRegions.size();
    }
}
