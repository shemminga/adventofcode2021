package com.sjoerdhemminga.adventofcode2021.day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final String[][] edges = lines.filter(not(String::isBlank))
                    .map(s -> s.split("-"))
                    .toArray(String[][]::new);

            final String[] vertices = Arrays.stream(edges)
                    .flatMap(Arrays::stream)
                    .distinct()
                    .toArray(String[]::new);

            final boolean[][] graph = makeGraph(vertices, edges);
            final boolean[] isLarge = markLarge(vertices);

            final int start = indexOf(vertices, "start");
            final int end = indexOf(vertices, "end");

            Printer.printGraph(graph, vertices, isLarge, start, end);

            final int count = countRoutesFromTo(graph, vertices, isLarge, start, end);
            System.out.println("count = " + count);
        }
    }

    private static int countRoutesFromTo(final boolean[][] graph, final String[] vertices, final boolean[] isLarge,
            final int start, final int end) {
        final AtomicInteger routeCounter = new AtomicInteger();

        route(graph, vertices, isLarge, new boolean[vertices.length], false, new ArrayDeque<>(), routeCounter, start,
                start, end);

        return routeCounter.get();
    }

    private static void route(final boolean[][] graph, final String[] vertices, final boolean[] isLarge,
            final boolean[] visited, final boolean hadDoubleVisit, final Deque<Integer> route,
            final AtomicInteger routeCounter, final int current, final int start, final int end) {
        if (!isAllowedVisit(isLarge, visited, hadDoubleVisit, current, start)) return;

        final boolean newHadDoubleVisit = hadDoubleVisit || (!isLarge[current] && visited[current]);
        final boolean origVisited = visited[current];
        visited[current] = true;
        route.push(current);

        if (current == end) {
            routeCounter.incrementAndGet();
            Printer.printRoute(route, routeCounter, vertices);
        } else
            for (int i = 0; i < graph[current].length; i++)
                if (graph[current][i])
                    route(graph, vertices, isLarge, visited, newHadDoubleVisit, route, routeCounter, i, start, end);

        route.pop();
        visited[current] = origVisited;
    }

    private static boolean isAllowedVisit(final boolean[] isLarge, final boolean[] visited,
            final boolean hadDoubleVisit, final int current, final int start) {
        if (isLarge[current]) return true;

        if (visited[current])
            return current != start && !hadDoubleVisit;

        return true;
    }

    private static boolean[][] makeGraph(final String[] vertices, final String[][] edges) {
        final boolean[][] graph = new boolean[vertices.length][vertices.length];

        Arrays.stream(edges)
                .forEach(edge -> {
                    final int v1 = indexOf(vertices, edge[0]);
                    final int v2 = indexOf(vertices, edge[1]);
                    graph[v1][v2] = true;
                    graph[v2][v1] = true;
                });

        return graph;
    }

    private static <T> int indexOf(final T[] xs, final T x) {
        for (int i = 0; i < xs.length; i++)
            if (x.equals(xs[i])) return i;
        throw new AssertionError();
    }

    private static boolean[] markLarge(final String[] vertices) {
        final boolean[] isLarge = new boolean[vertices.length];

        for (int i = 0; i < vertices.length; i++)
            isLarge[i] = vertices[i].toUpperCase().equals(vertices[i]);

        return isLarge;
    }
}
