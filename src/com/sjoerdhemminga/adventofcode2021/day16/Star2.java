package com.sjoerdhemminga.adventofcode2021.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    private static final int SUM = 0;
    private static final int PRODUCT = 1;
    private static final int MINIMUM = 2;
    private static final int MAXIMUM = 3;
    private static final int LITERAL = 4;
    private static final int GREATER_THAN = 5;
    private static final int LESS_THAN = 6;
    private static final int EQUAL_TO = 7;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final String data = lines.filter(not(String::isBlank))
                    .map(Hex2Bin::convert)
                    .findAny()
                    .orElseThrow();

            final Packet packet = Parser.parse(data);

            System.out.println("packet = " + packet);
            System.out.println("getValue(packet) = " + getValue(packet));
        }
    }

    private static List<Long> getValues(final List<Packet> packets) {
        return packets.stream()
                .map(Star2::getValue)
                .toList();
    }

    private static long getValue(final Packet packet) {
        final List<Long> subvalues = getValues(packet.subpackets());
        final LongStream substream = subvalues.stream()
                .mapToLong(x -> x);
        return switch (packet.typeId()) {
            case SUM -> substream.sum();
            case PRODUCT -> substream.reduce(1, (x, y) -> x * y);
            case MINIMUM -> substream.min().orElseThrow();
            case MAXIMUM -> substream.max().orElseThrow();
            case LITERAL -> packet.literalValue();
            case GREATER_THAN -> subvalues.get(0) > subvalues.get(1) ? 1 : 0;
            case LESS_THAN -> subvalues.get(0) < subvalues.get(1) ? 1 : 0;
            case EQUAL_TO -> subvalues.get(0).equals(subvalues.get(1)) ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected value: " + packet.typeId());
        };
    }
}
