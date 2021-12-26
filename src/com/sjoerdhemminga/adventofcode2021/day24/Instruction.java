package com.sjoerdhemminga.adventofcode2021.day24;

import java.util.function.BiFunction;

enum Instruction {
    SET("="), // pseudo-op for setting a constant in the analyzer
    INP("↓"),
    ADD("+", Long::sum),
    MUL("*", (x, y) -> x * y),
    DIV("/", (x, y) -> x / y),
    MOD("%", (x, y) -> x % y),
    NEQ("!=", (x, y) -> x.equals(y) ? 0L : 1L), // pseudo-op for very popular construction
    EQL("==", (x, y) -> x.equals(y) ? 1L : 0L);

    private final String token;
    private final BiFunction<Long, Long, Long> operation;

    Instruction(final String token) {
        this(token, (x, y) -> {throw new AssertionError();});
    }

    Instruction(final String token, final BiFunction<Long, Long, Long> operation) {
        this.token = token;
        this.operation = operation;
    }

    String token() {
        return token;
    }

    long apply(final long x, final long y) {
        return operation.apply(x, y);
    }

    static Instruction ofString(final String s) {
        return valueOf(s.toUpperCase());
    }
}
