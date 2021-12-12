package com.sjoerdhemminga.adventofcode2021.day12;

import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

final class Printer {
    static void printRoute(final Deque<Integer> route, final AtomicInteger routeCounter, final String[] vertices) {
        final List<String> vertexNames = route.stream()
                .map(i -> vertices[i])
                .collect(Collectors.toList());

        Collections.reverse(vertexNames);
        final String formattedRoute = String.join(",", vertexNames);

        System.out.printf("Route %3d found: %s%n", routeCounter.get(), formattedRoute);
    }

    static void printGraph(final boolean[][] graph, final String[] vertices, final boolean[] isLarge, final int start,
            final int end) {
        printVertices(vertices, isLarge, start, end);
        printGraph(graph);
    }

    private static void printGraph(final boolean[][] graph) {
        System.out.print("     ");
        for (int j = 0; j < graph[0].length; j++) System.out.printf("%3d", j);
        System.out.println();

        for (int i = 0; i < graph.length; i++) {
            System.out.printf("%3d: ", i);
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print("  " + (graph[i][j] ? '*' : '.'));
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printVertices(final String[] vertices, final boolean[] isLarge, final int start,
            final int end) {
        for (int i = 0; i < vertices.length; i++)
            System.out.printf("%2d: %5s %s%s%s%n",
                    i,
                    vertices[i], isLarge[i] ? "L" : "",
                    i == start ? "S" : "",
                    i == end ? "E" : "");
    }
}
