package com.sjoerdhemminga.adventofcode2021.day24;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.sjoerdhemminga.adventofcode2021.day24.Register.W;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.X;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.Y;
import static com.sjoerdhemminga.adventofcode2021.day24.Register.Z;

final class Executor {
    static Map<Register, Long> execute(final List<Line> lines, final List<Long> inputs) {
        final Map<Register, Long> registers = new EnumMap<>(Register.class);
        registers.put(W, 0L);
        registers.put(X, 0L);
        registers.put(Y, 0L);
        registers.put(Z, 0L);
        final Deque<Long> inputsDeque = new ArrayDeque<>(inputs);

        lines.forEach(line -> execute(line, registers, inputsDeque));
        return registers;
    }

    private static void execute(final Line line, final Map<Register, Long> registers, final Deque<Long> inputs) {
        switch (line.instruction()) {
        case SET, NEQ -> throw new AssertionError();
        case INP -> registers.put(line.paramA(), inputs.removeFirst());
        case ADD, MUL, DIV, MOD, EQL -> {
            final long valA = registers.get(line.paramA());
            final long valB;
            if (line.paramBreg() != null) valB = registers.get(line.paramBreg());
            else valB = line.paramBint();

            final long newA = line.instruction().apply(valA, valB);
            registers.put(line.paramA(), newA);
        }
        }
    }
}
