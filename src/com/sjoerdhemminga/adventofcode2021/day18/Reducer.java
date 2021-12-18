package com.sjoerdhemminga.adventofcode2021.day18;

final class Reducer {
    static SnailfishNumber reduce(final SnailfishNumber nr) {
        final SNTree snTree = new SNTree(null, nr);

        boolean changed = true;
        while(changed) {
            changed = explodeIfPossible(snTree, 0);
            if (changed) continue;
            changed = splitIfPossible(snTree);
        }

        return snTree.number();
    }

    private static boolean splitIfPossible(final SNTree snTree) {
        if (!snTree.isPair() && snTree.regularNumber() >= 10) {
            final int regNr = snTree.regularNumber();
            final int leftNr = regNr / 2;
            final int rightNr = regNr - leftNr;

            final SnailfishNumber leftSN = SnailfishNumber.ofRegularNumber(leftNr);
            final SnailfishNumber rightSN = SnailfishNumber.ofRegularNumber(rightNr);
            final SnailfishNumber newSN = SnailfishNumber.ofPair(leftSN, rightSN);

            snTree.number(newSN);
            return true;
        }

        if (!snTree.isPair()) return false;

        return splitIfPossible(snTree.leftTree()) || splitIfPossible(snTree.rightTree());
    }

    private static boolean explodeIfPossible(final SNTree snTree, final int parentCount) {
        if (!snTree.isPair()) return false;

        if (parentCount == 4) {
            explode(snTree);
            return true;
        }
        return explodeIfPossible(snTree.leftTree(), parentCount + 1) ||
                explodeIfPossible(snTree.rightTree(), parentCount + 1);
    }

    private static void explode(final SNTree snTree) {
        if (!snTree.isPair()) throw new AssertionError();

        final SNTree snTreeLeft = findSNTreeLeft(snTree);
        if (snTreeLeft != null) addTo(snTree.leftTree(), snTreeLeft);

        final SNTree snTreeRight = findSNTreeRight(snTree);
        if (snTreeRight != null) addTo(snTree.rightTree(), snTreeRight);

        snTree.number(SnailfishNumber.ofRegularNumber(0));
    }

    private static void addTo(final SNTree number, final SNTree toTree) {
        if (toTree.isPair()) throw new AssertionError();
        if (number.isPair()) throw new AssertionError();

        final int regNumber = toTree.regularNumber() + number.regularNumber();

        toTree.number(SnailfishNumber.ofRegularNumber(regNumber));
    }

    private static SNTree findSNTreeLeft(final SNTree snTree) {
        SNTree curr = snTree;
        while (curr != curr.parent().rightTree()) {
            curr = curr.parent();
            if (curr.parent() == null) return null; // Left-most tree
        }
        return recurseRightMost(curr.parent().leftTree());
    }

    private static SNTree recurseRightMost(final SNTree snTree) {
        SNTree curr = snTree;
        while (curr.rightTree() != null) curr = curr.rightTree();
        return curr;
    }

    private static SNTree findSNTreeRight(final SNTree snTree) {
        SNTree curr = snTree;
        while (curr != curr.parent().leftTree()) {
            curr = curr.parent();
            if (curr.parent() == null) return null; // Right-most tree
        }
        return recurseLeftMost(curr.parent().rightTree());
    }

    private static SNTree recurseLeftMost(final SNTree snTree) {
        SNTree curr = snTree;
        while (curr.leftTree() != null) curr = curr.leftTree();
        return curr;
    }
}
