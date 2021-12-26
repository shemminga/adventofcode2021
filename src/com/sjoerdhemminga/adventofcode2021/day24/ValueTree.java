package com.sjoerdhemminga.adventofcode2021.day24;

import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.INP;
import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.SET;
import static com.sjoerdhemminga.adventofcode2021.day24.Type.COMBINATION;
import static com.sjoerdhemminga.adventofcode2021.day24.Type.CONSTANT;
import static com.sjoerdhemminga.adventofcode2021.day24.Type.INPUT;

record ValueTree(ValueTree leftParent, ValueTree rightParent, Instruction instruction, Type type, long value) {
    static ValueTree forConstant(final long constant) {
        return new ValueTree(null, null, SET, CONSTANT, constant);
    }

    static ValueTree forInput(final int inputDigitNr) {
        return new ValueTree(null, null, INP, INPUT, inputDigitNr);
    }

    static ValueTree forOperation(final ValueTree left, final ValueTree right, final Instruction instruction) {
        return new ValueTree(left, right, instruction, COMBINATION, 0);
    }

    ValueTree combine(final ValueTree other, final Instruction instr) {
        return OptimizingCombiner.combine(this, other, instr);
    }

    Range range() {
        return new Range(minValue(), maxValue());
    }

    private long minValue() {
        return switch (instruction) {
            case SET -> value;
            case INP -> 1;
            case ADD, MUL -> instruction.apply(leftParent.minValue(), rightParent.minValue());
            case DIV -> leftParent.minValue() / rightParent.maxValue();
            case MOD -> leftParent.minValue() < rightParent.minValue() ? leftParent.minValue() : 0;
            case NEQ -> leftParent.range().isDisjointWith(rightParent.range()) ? 1 : 0;
            case EQL -> leftParent.range().isSingleValue() && leftParent().range().equals(rightParent.range()) ? 1 : 0;
        };
    }

    private long maxValue() {
        return switch (instruction) {
            case SET -> value;
            case INP -> 9;
            case ADD, MUL -> instruction.apply(leftParent.maxValue(), rightParent.maxValue());
            case DIV -> leftParent.maxValue() / rightParent.minValue();
            case MOD -> Math.min(leftParent.maxValue(), rightParent.maxValue());
            case NEQ -> leftParent.range().isSingleValue() && leftParent().range().equals(rightParent.range()) ? 0 : 1;
            case EQL -> leftParent.range().isDisjointWith(rightParent.range()) ? 0 : 1;
        };
    }

    boolean isConstant() {
        return type == CONSTANT;
    }

    @Override
    public String toString() {
        return switch (type) {
            case CONSTANT -> String.valueOf(value);
            case INPUT -> formatInput(value);
            case COMBINATION -> "(" + leftParent + " " + instruction.token() + " " + rightParent + ")";
        };
    }

    private static String formatInput(final long value) {
        if (value > 20) throw new AssertionError();
        //return "↓" + (value >= 10 ? "₁" : "") + new String(new int[]{0x2080 + (value % 10)}, 0, 1);
        return "I_" + value;
    }
}
