package com.sjoerdhemminga.adventofcode2021.day22;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

record Instruction(OnOff onOff, Cuboid cuboid) {
    private static final Pattern COORDS_SPLIT_PATTERN = Pattern.compile("\\.\\.");

    static Instruction ofString(final String line) {
        final String[] parts = line.split(" ");

        final Cuboid cuboid = createCuboid(parts);
        return new Instruction(OnOff.ofString(parts[0]), cuboid);
    }

    private static Cuboid createCuboid(final String[] parts) {
        final String[] coordParts = parts[1].split(",");
        final int[] xs = getCoords(coordParts[0]);
        final int[] ys = getCoords(coordParts[1]);
        final int[] zs = getCoords(coordParts[2]);
        return new Cuboid(xs[0], xs[1], ys[0], ys[1], zs[0], zs[1]);
    }

    private static int[] getCoords(final String coordString) {
        final String[] coords = COORDS_SPLIT_PATTERN.split(coordString.substring(2));
        return new int[]{parseInt(coords[0]), parseInt(coords[1])};
    }

    enum OnOff {
        ON, OFF;

        static OnOff ofString(final String string) {
            return switch (string) {
                case "on" -> ON;
                case "off" -> OFF;
                default -> throw new AssertionError();
            };
        }
    }
}
