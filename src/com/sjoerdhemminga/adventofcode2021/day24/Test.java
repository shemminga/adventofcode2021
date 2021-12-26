package com.sjoerdhemminga.adventofcode2021.day24;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.sjoerdhemminga.adventofcode2021.day24.Register.Z;

public final class Test {
    private static final Random RANDOM = new Random();

    public static void main(final String... args) throws URISyntaxException, IOException {
        final List<Line> lines = CodeReader.read("input.txt");

        for (int i = 0; i < 1000; i++)
            test(lines);
    }

    private static void test(final List<Line> lines) {
        final Map<Integer, Long> setInputs = new HashMap<>();
        final List<Long> inputs = new ArrayList<>();

        for (int i = 1; i <= 14; i++) {
            final long newInput = RANDOM.nextLong(1, 9);
            setInputs.put(i, newInput);
            inputs.add(newInput);
        }

        if (RANDOM.nextBoolean() && RANDOM.nextBoolean()) {
            final int inputToRemove = RANDOM.nextInt(1, 14);
            setInputs.remove(inputToRemove);
            System.out.println("Removed input " + inputToRemove);
        }

        final long start = System.nanoTime();
        final Map<Register, Long> executed = Executor.execute(lines, inputs);
        final long mid = System.nanoTime();
        final Deque<AnalyzedLine> analyzed = Analyzer.analyze(lines, setInputs);
        final long end = System.nanoTime();

        if (setInputs.size() == 14) {
            final long excTime = mid - start;
            final long anaTime = end - mid;
            System.out.printf("Executor time: %10d; Analyzer time: %10d; Winner: %s; Difference: %d\n",
                    excTime, anaTime, excTime == anaTime ? "Tie" : (excTime < anaTime ? "Executor" : "Analyzer"),
                    anaTime - excTime);
        } else System.out.println("Run done, comparing...");

        final ValueTree zValueTree = analyzed.peekLast()
                .registerStates()
                .get(Z);
        final long zValue = executed.get(Z);

        final Range analyzedRange = zValueTree.range();
        final Range executedRange = new Range(zValue, zValue);

        if (setInputs.size() == 14 && !analyzedRange.isSingleValue())
            error("Expected single value", analyzedRange, zValue, inputs);
        if (analyzedRange.isDisjointWith(executedRange)) error("Results differ", analyzedRange, zValue, inputs);
    }

    private static void error(final String message, final Range analyzedRange, final long executedResult,
            final List<Long> inputs) {
        System.err.printf("Error: %s analyzed range %s; executed result: %d; inputs: %s%n",
                message,
                analyzedRange,
                executedResult,
                inputs);
    }
}
