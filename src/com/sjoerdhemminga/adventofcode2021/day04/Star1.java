package com.sjoerdhemminga.adventofcode2021.day04;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Star1 {
    private static final int BOARD_SIZE = 5;
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> linesStream = Files.lines(Paths.get(input.toURI()))) {
            final Iterator<String> lines = linesStream.iterator();

            final Deque<Integer> numbers = Arrays.stream(lines.next()
                            .split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayDeque::new));

            lines.next();

            final List<int[][]> boards = getBoards(lines);

            playBingo(numbers, boards);
        }
    }

    private static void playBingo(final Deque<Integer> numbers, final List<int[][]> boards) {
        final List<boolean[][]> marked = boards.stream()
                .map(board -> new boolean[board.length][board[0].length])
                .toList();

        OptionalInt winningBoard = OptionalInt.empty();
        int drawn = -1;
        while (winningBoard.isEmpty()) {
            drawn = numbers.pop();
            markBoards(drawn, marked, boards);
            winningBoard = getWinningBoard(marked);
        }

        final int winnerIdx = winningBoard.getAsInt();

        printWinner(drawn, marked.get(winnerIdx), boards.get(winnerIdx));
    }

    private static void printWinner(final int lastDrawn, final boolean[][] marked, final int[][] board) {
        final int[] unmarkedNumbers = IntStream.range(0, board.length)
                .flatMap(i -> IntStream.range(0, board[i].length)
                        .filter(j -> !marked[i][j])
                        .map(j -> board[i][j]))
                .toArray();

        final int unmarkedSum = Arrays.stream(unmarkedNumbers)
                .sum();

        final int product = lastDrawn * unmarkedSum;

        System.out.println("unmarkedNumbers = " + Arrays.toString(unmarkedNumbers));
        System.out.println("lastDrawn = " + lastDrawn);
        System.out.println("unmarkedSum = " + unmarkedSum);
        System.out.println("product = " + product);
    }

    private static OptionalInt getWinningBoard(final List<boolean[][]> marked) {
        return IntStream.range(0, marked.size())
                .filter(i -> isWinningBoard(marked.get(i)))
                .findAny();
    }

    private static boolean isWinningBoard(final boolean[][] marked) {
        for (final boolean[] row : marked) {
            boolean winningRow = true;
            for (final boolean mark : row) winningRow &= mark;
            if (winningRow) return true;
        }

        for (int j = 0; j < marked[0].length; j++) {
            boolean winningCol = true;
            for (final boolean[] row : marked) winningCol &= row[j];
            if (winningCol) return true;
        }

        return false;
    }

    private static void markBoards(final int drawn, final List<boolean[][]> marked, final List<int[][]> boards) {
        IntStream.range(0, boards.size())
                .forEach(i -> markBoard(drawn, marked.get(i), boards.get(i)));
    }

    private static void markBoard(final int drawn, final boolean[][] marked, final int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == drawn) marked[i][j] = true;
            }
        }
    }

    private static List<int[][]> getBoards(final Iterator<String> lines) {
        final List<int[][]> boards = new ArrayList<>();
        Optional<int[][]> board;
        do {
            board = getBoard(lines);
            board.ifPresent(boards::add);
        } while(board.isPresent());

        return boards;
    }

    private static Optional<int[][]> getBoard(final Iterator<String> lines) {
        if (!lines.hasNext()) return Optional.empty();

        final int[][] board = new int[BOARD_SIZE][];

        for (int i = 0; i < BOARD_SIZE; i++) {
            final String line = lines.next()
                    .strip();

            board[i] = Arrays.stream(SPLIT_PATTERN.split(line))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        if (lines.hasNext()) lines.next();

        return Optional.of(board);
    }
}
