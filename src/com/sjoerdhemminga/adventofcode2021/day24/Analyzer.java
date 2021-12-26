package com.sjoerdhemminga.adventofcode2021.day24;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

final class Analyzer {
    static Deque<AnalyzedLine> analyze(final List<Line> lines, final Map<Integer, Long> setInputs) {
        final Deque<AnalyzedLine> analyzedLines = new ArrayDeque<>();
        analyzedLines.addLast(AnalyzedLine.INITIAL_STATE);

        lines.forEach(line -> {
            final AnalyzedLine analyzedLine = analyzedLines.peekLast()
                    .nextLine(line, setInputs);
            analyzedLines.addLast(analyzedLine);
        });

        return analyzedLines;
    }
}
