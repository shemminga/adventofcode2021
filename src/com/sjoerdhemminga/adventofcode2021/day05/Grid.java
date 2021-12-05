package com.sjoerdhemminga.adventofcode2021.day05;

final class Grid {
    private static final int GRID_SIZE = 1000;
    private final int[][] grid = new int[GRID_SIZE][GRID_SIZE];

    private int width = 0;
    private int height = 0;

    void addLine(final Line line) {
        final int xStep;
        final int yStep;

        width = Math.max(width, Math.max(line.getX1(), line.getX2()));
        height = Math.max(height, Math.max(line.getX1(), line.getX2()));

        if (line.isPoint()) {
            xStep = 0;
            yStep = 0;
        } else if (line.isHorizontal()) {
            xStep = line.getX1() > line.getX2() ? -1 : 1;
            yStep = 0;
        } else if (line.isVertical()) {
            xStep = 0;
            yStep = line.getY1() > line.getY2() ? -1 : 1;
        } else {
            xStep = line.getX1() > line.getX2() ? -1 : 1;
            yStep = line.getY1() > line.getY2() ? -1 : 1;
        }

        int x = line.getX1();
        int y = line.getY1();

        while(line.isOnLine(x, y)) {
            grid[y][x]++;
            x += xStep;
            y += yStep;
        }
    }

    int getMax() {
        int max = 0;
        for (int i = 0; i <= height; i++)
            for (int j = 0; j <= width; j++)
                max = Math.max(max, grid[i][j]);
        return max;
    }

    int countMinValue(final int val) {
        int count = 0;
        for (int i = 0; i <= height; i++)
            for (int j = 0; j <= width; j++)
                if (grid[i][j] >= val)
                    count++;
        return count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                if (grid[i][j] == 0) sb.append('.');
                else sb.append(grid[i][j]);
            }
            sb.append('\n');
        }

        return sb.toString();
    }
}
