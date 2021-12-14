package com.sjoerdhemminga.adventofcode2021.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class PairReactor {
    private final Map<String, Long> pairs = new HashMap<>();
    private final Map<String, List<String>> reactions = new HashMap<>();
    private char firstChar;

    void setFirstChar(final char firstChar) {
        this.firstChar = firstChar;
    }

    void addPair(final String pair) {
        addPair(pairs, pair, 1L);
    }

    private static void addPair(final Map<String, Long> pairs, final String pair, final long count) {
        pairs.put(pair, pairs.getOrDefault(pair, 0L) + count);
    }

    void addReaction(final String pair, final char insertion) {
        reactions.put(pair, List.of(
                String.valueOf(pair.charAt(0)) + insertion,
                String.valueOf(insertion) + pair.charAt(1)
        ));
    }

    void react() {
        final Map<String, Long> newCounts = new HashMap<>();

        reactions.forEach((oldPair, newPairs) -> {
            final Long currentCount = pairs.getOrDefault(oldPair, 0L);
            pairs.remove(oldPair);
            newPairs.forEach(p -> addPair(newCounts, p, currentCount));
        });

        pairs.putAll(newCounts);
    }

    Map<Character, Long> getHistogram() {
        final Map<Character, Long> histogram = new HashMap<>();
        histogram.put(firstChar, 1L);

        pairs.forEach((pair, count) -> {
            final Character c = pair.charAt(1);
            final Long currentCount = histogram.getOrDefault(c, 0L);
            histogram.put(c, currentCount + count);
        });

        return histogram;
    }
}
