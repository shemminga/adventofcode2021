package com.sjoerdhemminga.adventofcode2021.day24;

import java.util.List;

import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.ADD;
import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.DIV;
import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.EQL;
import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.MOD;
import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.MUL;
import static com.sjoerdhemminga.adventofcode2021.day24.Instruction.NEQ;
import static com.sjoerdhemminga.adventofcode2021.day24.ValueTree.forConstant;
import static com.sjoerdhemminga.adventofcode2021.day24.ValueTree.forOperation;

final class OptimizingCombiner {
    static ValueTree combine(final ValueTree leftParent, final ValueTree rightParent, final Instruction instr) {
        final ValueTree left;
        final ValueTree right;
        // These operations are communative, put the constant on the right
        if (leftParent.isConstant() && List.of(ADD, MUL, NEQ, EQL).contains(instr)) {
            right = leftParent;
            left = rightParent;
        } else {
            left = leftParent;
            right = rightParent;
        }

        if (left.isConstant() && right.isConstant()) {
            return constantFolded(instr, left.value(), right.value());
        }

        final ValueTree noopsEliminated = eliminateNoops(left, right, instr);
        if (noopsEliminated != null) return noopsEliminated;

        final ValueTree neqInserted = insertNeq(left, right, instr);
        if (neqInserted != null) return neqInserted;

        final ValueTree addEarlyEval = earlyEvaluateCommunativeInstruction(left, right, instr, ADD);
        if (addEarlyEval != null) return addEarlyEval;
        final ValueTree mulEarlyEval = earlyEvaluateCommunativeInstruction(left, right, instr, MUL);
        if (mulEarlyEval != null) return  mulEarlyEval;

        final ValueTree maddReordered = reorderMultiplyAdd(left, right, instr);
        if (maddReordered != null) return maddReordered;

        final ValueTree unknownsMovedAdd = moveUnkownsLeft(left, right, instr, ADD);
        if (unknownsMovedAdd != null) return unknownsMovedAdd;
        final ValueTree unknownsMovedMul = moveUnkownsLeft(left, right, instr, MUL);
        if (unknownsMovedMul != null) return unknownsMovedMul;

        return forOperation(left, right, instr);
    }

    private static ValueTree moveUnkownsLeft(final ValueTree left, final ValueTree right, final Instruction instr,
            final Instruction commInstr) {
        if (left.instruction() == commInstr && right.instruction() == commInstr && instr == commInstr)
            if (left.rightParent().isConstant() && right.rightParent().isConstant()) {
                final ValueTree newLeft =
                        forOperation(left.leftParent(), right.leftParent(), commInstr);
                final ValueTree newRight = forConstant(commInstr.apply(left.rightParent().value(),
                        right.rightParent().value()));

                return forOperation(newLeft, newRight, commInstr);
            }
        return null;
    }

    private static ValueTree reorderMultiplyAdd(final ValueTree left, final ValueTree right, final Instruction instr) {
        if (instr == MUL && left.instruction() == ADD && right.isConstant() && left.rightParent().isConstant()) {
            final ValueTree newLeft = forOperation(left.leftParent(), right, MUL);
            final ValueTree newRight = forConstant(MUL.apply(left.rightParent().value(), right.value()));
            return forOperation(newLeft, newRight, ADD);
        }
        return null;
    }

    private static ValueTree insertNeq(final ValueTree left, final ValueTree right, final Instruction instr) {
        if (instr == EQL && left.instruction() == EQL) if (right.isConstant()) {
            if (right.value() == 0) return forOperation(left.leftParent(), left.rightParent(), NEQ);
            if (right.value() == 1) throw new UnsupportedOperationException();
            throw new AssertionError();
        }
        return null;
    }

    private static ValueTree earlyEvaluateCommunativeInstruction(final ValueTree left, final ValueTree right,
            final Instruction instr, final Instruction commInstr) {
        if (instr == commInstr && right.isConstant() && left.instruction() == commInstr && left.rightParent()
                .isConstant()) {
            return forOperation(left.leftParent(),
                    constantFolded(commInstr, left.rightParent().value(), right.value()),
                    commInstr);
        }
        return null;
    }

    private static ValueTree eliminateNoops(final ValueTree left, final ValueTree right, final Instruction instr) {
        // Multiplying by 0 is 0, multiplying by 1 is an identity operation.
        if (instr == MUL && right.isConstant()) {
            if (right.value() == 0) return forConstant(0);
            if (right.value() == 1) return left;
        }

        // Dividing 0 by anything is 0, and dividing anything by 1 changes nothing.
        if (instr == DIV && left.isConstant()) {
            if (left.value() == 0) return forConstant(0);
        } else if (instr == DIV && right.isConstant()) {
            if (right.value() == 1) return left;
        }

        // Adding 0 does nothing.
        if (instr == ADD && right.isConstant()) {
            if (right.value() == 0) return left;
        }

        final Range leftRange = left.range();
        final Range rightRange = right.range();

        // If the ranges don't overlap, an EQL can never be true.
        if (instr == EQL && leftRange.isDisjointWith(rightRange)) return forConstant(0);

        // If both sides support only one value, evaluation becomes trivial
        if (instr == NEQ) {
            if (leftRange.isSingleValue() && rightRange.isSingleValue()) {
                if (leftRange.equals(rightRange)) return forConstant(0);
                return forConstant(1);
            }
        }

        // A mod operation that never has an input larger than the divisor will never change its input.
        if (instr == MOD && leftRange.maxValue() < rightRange.minValue()) return left;

        return null;
    }

    private static ValueTree constantFolded(final Instruction instr, final long value, final long otherValue) {
        return forConstant(instr.apply(value, otherValue));
    }
}
