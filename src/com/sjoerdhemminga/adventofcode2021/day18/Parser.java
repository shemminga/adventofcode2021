package com.sjoerdhemminga.adventofcode2021.day18;

final class Parser {
    static SnailfishNumber parse(final String input) {
        return input.startsWith("[") ? parsePair(input) : parseRegularNumber(input);
    }

    private static SnailfishNumber parseRegularNumber(final String input) {
        final int endIdxExcl = findNextNonNumber(input);
        final String nrStr = input.substring(0, endIdxExcl);
        final int regularNumber = Integer.parseInt(nrStr);
        return SnailfishNumber.ofRegularNumber(regularNumber);
    }

    private static int findNextNonNumber(final String input) {
        for (int i = 0; i < input.length(); i++)
            if (!Character.isDigit(input.codePointAt(i))) return i;

        return input.length();
    }

    private static SnailfishNumber parsePair(final String input) {
        final int commaPos = findMatchingComma(input);
        final int closePos = findMatchingClose(input, commaPos);

        final String leftStr = input.substring(1, commaPos);
        final String rightStr = input.substring(commaPos + 1, closePos);

        final SnailfishNumber leftNr = parse(leftStr);
        final SnailfishNumber rightNr = parse(rightStr);

        return SnailfishNumber.ofPair(leftNr, rightNr);
    }

    private static int findMatchingComma(final String input) {
        int level = 0;
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == '[') level++;
            else if (input.charAt(i) == ',' && level == 0) return i;
            else if (input.charAt(i) == ']') level--;
        }

        throw new IllegalArgumentException("level = " + level + " input = " + input);
    }

    private static int findMatchingClose(final String input, final int commaPos) {
        int level = 0;
        for (int i = commaPos; i < input.length(); i++) {
            if (input.charAt(i) == '[') level++;
            else if (input.charAt(i) == ']' && level == 0) return i;
            else if (input.charAt(i) == ']') level--;
        }

        throw new IllegalArgumentException("level = " + level + " input = " + input);
    }
}
