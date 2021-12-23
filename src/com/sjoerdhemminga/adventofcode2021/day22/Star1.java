package com.sjoerdhemminga.adventofcode2021.day22;

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
            final ReactorCore reactorCore = new ReactorCore();

            lines.filter(not(String::isBlank))
                    .map(Instruction::ofString)
                    .filter(instruction -> instruction.cuboid().fromX() >= -50)
                    .filter(instruction -> instruction.cuboid().fromY() >= -50)
                    .filter(instruction -> instruction.cuboid().fromZ() >= -50)
                    .filter(instruction -> instruction.cuboid().toX() >= -50)
                    .filter(instruction -> instruction.cuboid().toY() >= -50)
                    .filter(instruction -> instruction.cuboid().toZ() >= -50)
                    .filter(instruction -> instruction.cuboid().fromX() <= 50)
                    .filter(instruction -> instruction.cuboid().fromY() <= 50)
                    .filter(instruction -> instruction.cuboid().fromZ() <= 50)
                    .filter(instruction -> instruction.cuboid().toX() <= 50)
                    .filter(instruction -> instruction.cuboid().toY() <= 50)
                    .filter(instruction -> instruction.cuboid().toZ() <= 50)
                    .forEach(reactorCore::execute);

            System.out.println("reactorCore = " + reactorCore);
            System.out.println("reactorCore.cubesSwitchedOn() = " + reactorCore.cubesSwitchedOn());
        }
    }
}
