package com.sjoerdhemminga.adventofcode2021.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final String data = lines.filter(not(String::isBlank))
                    .map(Hex2Bin::convert)
                    .findAny()
                    .orElseThrow();

            final Packet packet = Parser.parse(data);

            System.out.println("packet = " + packet);
            System.out.println("sumVersionsDeep(packet) = " + sumVersionsDeep(List.of(packet)));
        }
    }

    private static long sumVersionsDeep(final List<Packet> packets) {
        return packets.stream()
                .mapToLong(p -> p.version() + sumVersionsDeep(p.subpackets()))
                .sum();
    }
}
