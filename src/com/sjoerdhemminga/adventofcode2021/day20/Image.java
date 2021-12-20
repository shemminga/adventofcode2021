package com.sjoerdhemminga.adventofcode2021.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

final class Image {
    private final List<char[]> image = new ArrayList<>();
    private char infiniteBorder = '.';

    char getInfiniteBorder() {
        return infiniteBorder;
    }

    void setInfiniteBorder(final char infiniteBorder) {
        this.infiniteBorder = infiniteBorder;
    }

    void addLine(final char[] line) {
        image.add(line);
    }

    int getLineSize() {
        return image.size();
    }

    int getColSize() {
        return image.get(0).length;
    }

    char[][] getMatrixFor(final int line, final int col) {
        final char[][] matrix = new char[3][3];

        if (line <= 0) Arrays.fill(matrix[0], infiniteBorder);
        else copyMatrixLine(line - 1, col, matrix[0]);

        if (line == -1 || line == image.size()) Arrays.fill(matrix[1], infiniteBorder);
        else copyMatrixLine(line, col, matrix[1]);

        if (line >= image.size() - 1) Arrays.fill(matrix[2], infiniteBorder);
        else copyMatrixLine(line + 1, col, matrix[2]);

        return matrix;
    }

    private void copyMatrixLine(final int line, final int centerCol, final char[] toMatrixLine) {
        final char[] imgLine = image.get(line);

        if (centerCol <= 0) toMatrixLine[0] = infiniteBorder;
        else toMatrixLine[0] = imgLine[centerCol - 1];

        if (centerCol == -1 || centerCol == imgLine.length) toMatrixLine[1] = infiniteBorder;
        else toMatrixLine[1] = imgLine[centerCol];

        if (centerCol >= imgLine.length - 1) toMatrixLine[2] = infiniteBorder;
        else toMatrixLine[2] = imgLine[centerCol + 1];
    }

    private int countLights() {
        return image.stream()
                .flatMapToInt(line -> new String(line).chars()
                        .map(c -> c == '#' ? 1 : 0))
                .sum();
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n");
        sj.add("Border: " + infiniteBorder);
        image.forEach(line -> sj.add(new String(line)));
        sj.add("Lights: " + countLights());
        return sj.toString() + '\n';
    }
}
