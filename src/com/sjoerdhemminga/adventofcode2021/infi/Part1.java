package com.sjoerdhemminga.adventofcode2021.infi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.function.Predicate.not;

public final class Part1 {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[,:]? ");

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Part1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Map<String, Map<String, Integer>> map = lines.filter(not(String::isBlank))
                    .skip(1)
                    .map(SPLIT_PATTERN::split)
                    .collect(Collectors.toMap(xs -> xs[0], Part1::toMap));

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

            counts.entrySet().forEach(System.out::println);
        }
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
