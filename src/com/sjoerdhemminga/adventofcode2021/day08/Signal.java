package com.sjoerdhemminga.adventofcode2021.day08;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

final class Signal {
    private static final Pattern INOUT_SPLIT_PATTERN = Pattern.compile(" \\| ");
    private final String[] in;
    private final String[] out;
    private final String[] digits = new String[10];
    private char top, topL, topR, mid, botL, botR, bot;
    private long value;

    private Signal(final String[] in, final String[] out) {
        this.in = in;
        this.out = out;

        decode();
        setValue();
    }

    private void setValue() {
        final List<String> digitList = Arrays.asList(digits);

        final String valStr = Arrays.stream(out)
                .mapToInt(digitList::indexOf)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

        value = Long.parseLong(valStr);
    }

    long getValue() {
        return value;
    }

    private void decode() {
        digits[1] = findNthOfLength(1, 2);
        digits[4] = findNthOfLength(1, 4);
        digits[7] = findNthOfLength(1, 3);
        digits[8] = findNthOfLength(1, 7);

        top = decodeTop();
        topR = decodeTopR();
        botR = decodeBotR();

        set5And6();

        botL = decodeBotL();

        set9();
        set0();

        mid = decodeMid();

        set2And3();

        topL = decodeTopL();
        bot = decodeBot();
    }

    private char decodeTop() {
        return (char) digits[7].chars()
                .filter(c -> digits[1].indexOf(c) < 0)
                .findAny()
                .orElseThrow();
    }

    private char decodeTopR() {
        final String one = digits[1];

        for (int i = 1; i <= 3; i++) {
            final String digit = findNthOfLength(i, 6);

            if (digit.indexOf(one.charAt(0)) < 0) return one.charAt(0);
            if (digit.indexOf(one.charAt(1)) < 0) return one.charAt(1);
        }

        throw new AssertionError();
    }

    private char decodeBotR() {
        return (char) digits[7].chars()
                .filter(c -> c != top)
                .filter(c -> c != topR)
                .findAny()
                .orElseThrow();
    }

    private void set5And6() {
        final String[] fiveAndSix = Arrays.stream(in)
                .filter(s -> s.indexOf(topR) < 0)
                .toArray(String[]::new);

        if (fiveAndSix[0].length() == 5 && fiveAndSix[1].length() == 6) {
            digits[5] = fiveAndSix[0];
            digits[6] = fiveAndSix[1];
        } else if (fiveAndSix[0].length() == 6 && fiveAndSix[1].length() == 5) {
            digits[5] = fiveAndSix[1];
            digits[6] = fiveAndSix[0];
        } else throw new AssertionError();
    }

    private char decodeBotL() {
        return (char) IntStream.range('a', 'g' + 1)
                .filter(c -> digits[5].indexOf(c) < 0 && digits[6].indexOf(c) >= 0)
                .findAny()
                .orElseThrow();
    }

    private void set9() {
        digits[9] = Arrays.stream(in)
                .filter(s -> s.length() == 6)
                .filter(s -> s.indexOf(botL) < 0)
                .findAny()
                .orElseThrow();
    }

    private void set0() {
        digits[0] = Arrays.stream(in)
                .filter(s -> s.length() == 6)
                .filter(not(digits[6]::equals))
                .filter(not(digits[9]::equals))
                .findAny()
                .orElseThrow();
    }

    private char decodeMid() {
        return (char) IntStream.range('a', 'g' + 1)
                .filter(c -> digits[0].indexOf(c) < 0)
                .findAny()
                .orElseThrow();
    }

    private void set2And3() {
        final String[] twoAndThree = Arrays.stream(in)
                .filter(s -> s.length() == 5)
                .filter(not(digits[5]::equals))
                .toArray(String[]::new);

        if (twoAndThree[0].indexOf(botR) < 0) {
            digits[2] = twoAndThree[0];
            digits[3] = twoAndThree[1];
        } else if (twoAndThree[1].indexOf(botR) < 0) {
            digits[2] = twoAndThree[1];
            digits[3] = twoAndThree[0];
        } else throw new AssertionError();
    }

    private char decodeTopL() {
        return (char) IntStream.range('a', 'g' + 1)
                .filter(c -> digits[2].indexOf(c) < 0)
                .filter(c -> digits[3].indexOf(c) < 0)
                .findAny()
                .orElseThrow();
    }

    private char decodeBot() {
        return (char) IntStream.range('a', 'g' + 1)
                .filter(c -> c != top)
                .filter(c -> c != topL)
                .filter(c -> c != topR)
                .filter(c -> c != mid)
                .filter(c -> c != botL)
                .filter(c -> c != botR)
                .findAny()
                .orElseThrow();
    }

    private String findNthOfLength(final int n, final int len) {
        int current = 0;
        for (final String signal : in)
            if (signal.length() == len) {
                current++;
                if (current == n) return signal;
            }
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Signal.class.getSimpleName() + "[", "]").add("in=" + Arrays.toString(in))
                .add("out=" + Arrays.toString(out))
                .add("top=" + top)
                .add("topL=" + topL)
                .add("topR=" + topR)
                .add("mid=" + mid)
                .add("botL=" + botL)
                .add("botR=" + botR)
                .add("bot=" + bot)
                .add("value=" + value)
                .toString();
    }

    static Signal fromLine(final String s) {
        final String[] inout = INOUT_SPLIT_PATTERN.split(s);

        final String[] in = sortStrings(inout[0].split(" "));
        final String[] out = sortStrings(inout[1].split(" "));

        return new Signal(in, out);
    }

    private static String[] sortStrings(final String[] strings) {
        return Arrays.stream(strings)
                .map(Signal::sortString)
                .toArray(String[]::new);
    }

    private static String sortString(final String s) {
        final char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
