package com.sjoerdhemminga.adventofcode2021.day17;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final Pattern DIR_SPLIT_PATTERN = Pattern.compile(", y=");
    private static final Pattern MIN_MAX_SPLIT_PATTERN = Pattern.compile(Pattern.quote(".."));

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] coords = lines.filter(not(String::isBlank))
                    .map(s -> s.substring(15))
                    .map(DIR_SPLIT_PATTERN::split)
                    .flatMap(Arrays::stream)
                    .map(MIN_MAX_SPLIT_PATTERN::split)
                    .flatMap(Arrays::stream)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final List<Integer> vxs = findHittingXVelocities(coords[0], coords[1]);
            final List<Integer> vys = findHittingYVelocities(coords[2], coords[3]);

            System.out.println("vxs = " + vxs);
            System.out.println("vys = " + vys);

            final int maxY = vxs.stream()
                    .flatMap(vx -> vys.stream()
                            .map(vy -> simulateToMaxY(coords[0], coords[1], coords[2], coords[3], vx, vy)))
                    .mapToInt(n -> n)
                    .max()
                    .orElseThrow();

            System.out.println("maxY = " + maxY);
        }
    }


    private static List<Integer> findHittingXVelocities(final int c1, final int c2) {
        final int v1, v2;

        if (c1 > 0 && c2 > 0) {
            v1 = 1;
            v2 = Math.max(c1, c2);
        } else throw new AssertionError();

        final List<Integer> hittingVs = new ArrayList<>();

        for (int v = v1; v <= v2; v++)
            if (simulateX(c1, c2, v)) hittingVs.add(v);

        return hittingVs;
    }

    private static List<Integer> findHittingYVelocities(final int c1, final int c2) {
        final int v1, v2;

        if (c1 < 0 && c2 < 0) {
            v1 = Math.min(c1, c2);
            v2 = -1; //@
        } else throw new AssertionError();

        final List<Integer> hittingVs = new ArrayList<>();

        for (int vy = v1; vy <= v2; vy++)
            if (simulateY(c1, c2, vy))
                hittingVs.add(vy);

        final List<Integer> allVs = new ArrayList<>(hittingVs);

        // Any shot up hits the same negative points as a shot down that goes 1 faster, and the reverse is true too
        hittingVs.forEach(v -> allVs.add(-v - 1));

        return allVs;
    }

    private static int simulateToMaxY(final int x1, final int x2, final int y1, final int y2, final int vx,
            final int vy) {
        int vxcurr = vx, vycurr = vy, xcurr = 0, ycurr = 0;
        int maxY = Integer.MIN_VALUE;

        while (vxcurr <= x2 && vycurr >= y1) {
            xcurr += vxcurr;
            ycurr += vycurr;
            vxcurr = updateVx(vxcurr);
            vycurr = updateVy(vycurr);

            maxY = Math.max(maxY, ycurr);
            if (xcurr >= x1 && xcurr <= x2 && ycurr >= y1 && ycurr <= y2) return maxY;
        }

        return Integer.MIN_VALUE;
    }

    private static boolean simulateX(final int c1, final int c2, final int vx) {
        int vcurr = vx;
        int posCurr = 0;

        while (posCurr <= c2 && vcurr != 0) {
            posCurr += vcurr;
            vcurr = updateVx(vcurr);

            if (posCurr >= c1 && posCurr <= c2) return true;
        }

        return false;
    }

    private static boolean simulateY(final int c1, final int c2, final int vy) {
        int vcurr = vy;
        int posCurr = 0;

        while (posCurr >= c1) {
            posCurr += vcurr;
            vcurr = updateVy(vcurr);

            if (posCurr >= c1 && posCurr <= c2) return true;
        }

        return false;
    }

    private static int updateVx(final int vcurr) {
        if (vcurr > 0) return vcurr - 1;
        if (vcurr < 0) return vcurr + 1;
        return 0;
    }

    private static int updateVy(final int vcurr) {
        return vcurr - 1;
    }
}
