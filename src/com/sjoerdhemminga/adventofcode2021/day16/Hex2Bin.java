package com.sjoerdhemminga.adventofcode2021.day16;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

final class Hex2Bin {
    private static final Map<Character, String> MAPPING =
            Map.ofEntries(
                    entry('0', "0000"),
                    entry('1', "0001"),
                    entry('2', "0010"),
                    entry('3', "0011"),
                    entry('4', "0100"),
                    entry('5', "0101"),
                    entry('6', "0110"),
                    entry('7', "0111"),
                    entry('8', "1000"),
                    entry('9', "1001"),
                    entry('A', "1010"),
                    entry('B', "1011"),
                    entry('C', "1100"),
                    entry('D', "1101"),
                    entry('E', "1110"),
                    entry('F', "1111"));

    static String convert(final String hex) {
        return hex.chars()
                .mapToObj(c -> (char) c)
                .map(MAPPING::get)
                .collect(Collectors.joining());
    }
}
