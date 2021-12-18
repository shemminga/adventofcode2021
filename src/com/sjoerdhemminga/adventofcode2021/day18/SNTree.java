package com.sjoerdhemminga.adventofcode2021.day18;

final class SNTree {
    private final SNTree parent;
    private SNTree leftTree;
    private SNTree rightTree;
    private int regularNumber;
    private boolean isPair;

    SNTree(final SNTree parent, final SnailfishNumber number) {
        this.parent = parent;
        number(number);
    }

    boolean isPair() {
        return isPair;
    }

    SNTree parent() {
        return parent;
    }

    SnailfishNumber number() {
        return isPair ? SnailfishNumber.ofPair(leftTree.number(), rightTree.number()) :
                SnailfishNumber.ofRegularNumber(regularNumber);
    }

    void number(final SnailfishNumber number) {
        isPair = number.isPair();
        if (isPair) {
            leftTree = new SNTree(this, number.left());
            rightTree = new SNTree(this, number.right());
            regularNumber = 0;
        } else {
            leftTree = rightTree = null;
            regularNumber = number.regularNumber();
        }
    }

    int regularNumber() {
        return regularNumber;
    }

    SNTree leftTree() {
        return leftTree;
    }

    void leftTree(final SNTree leftTree) {
        this.leftTree = leftTree;
    }

    SNTree rightTree() {
        return rightTree;
    }

    void rightTree(final SNTree rightTree) {
        this.rightTree = rightTree;
    }

    @Override
    public String toString() {
        return isPair ? ("[" + leftTree + ',' + rightTree + ']') : String.valueOf(regularNumber);
    }
}
