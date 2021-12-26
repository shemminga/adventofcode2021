package com.sjoerdhemminga.adventofcode2021.day24;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2021.day24.Range.ZERO;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.Z;
import static java.util.Map.entry;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final List<Line> lines = CodeReader.read("input.txt");

        final Map<Integer, Long> code = new HashMap<>();

        testHypothesis(lines, code, 1);

        System.out.println("Min model number: " + formatInputs(code));
    }

    private static boolean testHypothesis(final List<Line> lines, final Map<Integer, Long> hypothesis,
            final int inputNr) {
        if (inputNr >= 15) return true;

        final List<Long> acceptableValues = findAcceptableValues(inputNr, lines, hypothesis);

        if (inputNr >= 12)
            System.out.printf("With %s input %2d can be %s%n", formatInputs(hypothesis), inputNr, acceptableValues);

        for (final Long inputVal : acceptableValues) {
            hypothesis.put(inputNr, inputVal);
            if (testHypothesis(lines, hypothesis, inputNr + 1)) return true;
            hypothesis.remove(inputNr, inputVal);
        }

        return false;
    }

    private static String formatInputs(final Map<Integer, Long> inputs) {
        final char[] chars = new char[14];

        Arrays.fill(chars, '.');

        for (int i = 1; i <= 14; i++)
            if (inputs.containsKey(i))
                chars[i - 1] = (char) (inputs.get(i) + '0');

        return new String(chars);
    }

    private static List<Long> findAcceptableValues(final int inputNr, final List<Line> lines,
            final Map<Integer, Long> crackedInputs) {
        final List<Long> acceptableValues = new ArrayList<>();

        for (long inputVal = 1; inputVal <= 9; inputVal++) {
            final Deque<AnalyzedLine> analyzedLines =
                    Analyzer.analyze(lines, copyAdd(crackedInputs, inputNr, inputVal));

            final ValueTree valueTree = analyzedLines.peekLast()
                    .registerStates()
                    .get(Z);

            if (valueTree.range().intersectsWith(ZERO)) acceptableValues.add(inputVal);
        }

        return acceptableValues;
    }

    private static <K, V> Map<K, V> copyAdd(final Map<K, V> map, final K key, final V value) {
        return Stream.concat(map.entrySet()
                        .stream(), Stream.of(entry(key, value)))
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }
}
