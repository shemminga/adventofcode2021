package com.sjoerdhemminga.adventofcode2021.day24;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.sjoerdhemminga.adventofcode2021.day24.Register.W;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.X;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.Y;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.Z;
import static java.util.Map.entry;

record AnalyzedLine(Line line, Map<Register, ValueTree> registerStates, int inputsRead) {
    static  final AnalyzedLine INITIAL_STATE = new AnalyzedLine(null, Map.of(
            W, ValueTree.forConstant(0),
            X, ValueTree.forConstant(0),
            Y, ValueTree.forConstant(0),
            Z, ValueTree.forConstant(0)
    ), 0);

    AnalyzedLine nextLine(final Line nextLine, final Map<Integer, Long> setInputs) {
        return switch (nextLine.instruction()) {
            case SET -> throw new AssertionError();
            case INP -> forInput(nextLine, setInputs);
            case ADD, MUL, DIV, MOD, NEQ, EQL -> combine(nextLine);
        };
    }

    private AnalyzedLine combine(final Line nextLine) {
        final ValueTree aTreeIn = registerStates.get(nextLine.paramA());
        final ValueTree bTreeIn = nextLine.paramBreg() != null ? registerStates.get(nextLine.paramBreg()) :
                ValueTree.forConstant(nextLine.paramBint());

        final ValueTree aTreeOut = aTreeIn.combine(bTreeIn, nextLine.instruction());
        final Map<Register, ValueTree> newRegStates = copyReplace(registerStates, nextLine.paramA(), aTreeOut);
        return new AnalyzedLine(nextLine, newRegStates, inputsRead);
    }

    private AnalyzedLine forInput(final Line nextLine, final Map<Integer, Long> setInputs) {
        final int newInputsRead = inputsRead + 1;

        final ValueTree newValueTree;
        if (setInputs.containsKey(newInputsRead)) newValueTree = ValueTree.forConstant(setInputs.get(newInputsRead));
        else newValueTree = ValueTree.forInput(newInputsRead);

        final Map<Register, ValueTree> newRegStates = copyReplace(registerStates, nextLine.paramA(), newValueTree);
        return new AnalyzedLine(nextLine, newRegStates, newInputsRead);
    }

    private static <K, V> Map<K, V> copyReplace(final Map<K, V> map, final K key, final V value) {
        return map.entrySet()
                .stream()
                .map(e -> e.getKey()
                        .equals(key) ? entry(key, value) : e)
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }
}
