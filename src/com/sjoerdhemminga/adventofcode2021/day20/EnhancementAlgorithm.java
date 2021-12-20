package com.sjoerdhemminga.adventofcode2021.day20;

final class EnhancementAlgorithm {
    private char[] algorithm;

    void setAlgorithm(final char[] algorithm) {
        this.algorithm = algorithm;
    }

    Image enhance(final Image input) {
        final Image output = new Image();

        output.setInfiniteBorder(getNewBorder(input));

        final int newLineSize = input.getLineSize() + 2;
        final int newColSize = input.getColSize() + 2;

        for (int i = 0; i < newLineSize; i++) {
            final char[] newLine = new char[newColSize];
            for (int j = 0; j < newColSize; j++) {
                final char[][] matrix = input.getMatrixFor(i - 1, j - 1);
                final int idx = matrixToInt(matrix);
                newLine[j] = algorithm[idx];
            }
            output.addLine(newLine);
        }

        return output;
    }

    private char getNewBorder(final Image input) {
        final char oldBorder = input.getInfiniteBorder();
        final int newBorderIdx = oldBorder == '#' ? 511 : 0;
        return algorithm[newBorderIdx];
    }

    private static int matrixToInt(final char[][] matrix) {
        final StringBuilder sb = new StringBuilder();
        for (final char[] line : matrix)
            for (final char c : line)
                sb.append(c == '#' ? '1' : '0');
        return Integer.parseInt(sb.toString(), 2);
    }
}
