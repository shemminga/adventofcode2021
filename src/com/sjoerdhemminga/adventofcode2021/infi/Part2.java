package com.sjoerdhemminga.adventofcode2021.infi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static java.util.function.Predicate.not;

public final class Part2 {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[,:]? ");

    public static void main(final String... args) throws IOException, URISyntaxException {
        final int targetToyCount = 20;
        final URL input = Part2.class.getResource("input.txt");

        final List<String> lines = Files.readAllLines(Paths.get(input.toURI()));

        final String firstLine = lines.remove(0);
        final int missingPartsCount = Integer.parseInt(SPLIT_PATTERN.split(firstLine)[0]);

        final Map<String, Map<String, Integer>> map = lines.stream()
                .filter(not(String::isBlank))
                .map(SPLIT_PATTERN::split)
                .collect(Collectors.toMap(xs -> xs[0], Part2::toMap));

        final Map<String, Integer> counts = getCounts(cloneMap(map));
        final String[] toys = getToys(map, counts);

        final String[] solution = fill(targetToyCount, toys, missingPartsCount, counts);

        final String solutionString = Arrays.stream(solution)
                .sorted()
                .map(s -> s.substring(0, 1))
                .collect(Collectors.joining());

        System.out.println(solutionString);
    }

    private static String[] fill(final int targetToyCount, final String[] toys, final int missingPartsCount,
            final Map<String, Integer> counts) {
        final String[] solution = new String[targetToyCount];
        final boolean solved = fill(targetToyCount, toys, missingPartsCount, counts, solution, 0);

        if (!solved) throw new AssertionError();

        return solution;
    }

    private static boolean fill(final int targetToyCount, final String[] toys, final int missingPartsCount,
            final Map<String, Integer> counts, final String[] bag, final int bagIdx) {
        if (bagIdx >= bag.length) return false;

        for (final String toy : toys) {
            final int partsUsed = counts.get(toy);
            if (partsUsed > missingPartsCount) return false;

            bag[bagIdx] = toy;

            if (partsUsed == missingPartsCount && bagIdx + 1 == bag.length) return true; // Found

            final boolean solved =
                    fill(targetToyCount - 1, toys, missingPartsCount - partsUsed, counts, bag, bagIdx + 1);
            if (solved) return true;

            bag[bagIdx] = null;
        }

        return false;
    }

    private static Map<String, Map<String, Integer>> cloneMap(final Map<String, Map<String, Integer>> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> shallowCloneMap(e.getValue())));
    }

    private static Map<String, Integer> shallowCloneMap(final Map<String, Integer> m) {
        return m.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static String[] getToys(final Map<String, Map<String, Integer>> map,
            final Map<String, Integer> counts) {
        final Set<String> allParts = map.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .keySet()
                        .stream())
                .collect(Collectors.toSet());

        return map.keySet()
                .stream()
                .filter(not(allParts::contains))
                .sorted(Comparator.comparingInt(counts::get).reversed())
                .toArray(String[]::new);
    }

    private static Map<String, Integer> getCounts(final Map<String, Map<String, Integer>> map) {
        final Map<String, Integer> counts = new HashMap<>();

        while (counts.size() < map.size()) {
            map.forEach((item, parts) -> {
                final List<String> partNames = new ArrayList<>(parts.keySet());

                boolean changed = false;
                for (final String partName : partNames) {
                    final Integer number = parts.get(partName);
                    if (map.containsKey(partName)) {
                        changed = true;
                        map.get(partName)
                                .forEach((k, v) -> parts.merge(k, number * v, Integer::sum));
                        parts.remove(partName);
                    }
                }

                if (!changed) {
                    final int count = parts.values()
                            .stream()
                            .mapToInt(Integer::intValue)
                            .sum();
                    counts.put(item, count);
                }
            });
        }

        return counts;
    }

    private static Map<String, Integer> toMap(final String[] arr) {
        final List<Map.Entry<String, Integer>> entries = new LinkedList<>();
        for (int i = 1; i < arr.length; i += 2) {
            entries.add(entry(arr[i + 1], Integer.parseInt(arr[i])));
        }

        return entries.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
