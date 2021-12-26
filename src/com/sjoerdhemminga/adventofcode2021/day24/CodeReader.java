package com.sjoerdhemminga.adventofcode2021.day24;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2021.day24.Register.ofString;
import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

final class CodeReader {
    static List<Line> read(final String filename) throws URISyntaxException, IOException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> fileLines = Files.lines(Paths.get(input.toURI()))) {
            return fileLines.filter(not(String::isBlank))
                    .map(s -> s.split(" "))
                    .map(parts -> new Line(Instruction.ofString(parts[0]),
                            ofString(parts[1]),
                            parts.length > 2,
                            parts.length > 2 && parts[2].charAt(0) >= 'w' ? ofString(parts[2]) : null,
                            parts.length > 2 && parts[2].charAt(0) <= '9' ? parseInt(parts[2]) : 0))
                    //.limit(100)
                    .toList();
        }
    }
}
