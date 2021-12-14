package com.sjoerdhemminga.adventofcode2021.day14;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

final class LinkedListReactor {
    private final Map<String, Character> reactions = new HashMap<>();
    private LinkedList<Character> polymer = new LinkedList<>();

    String getPolymer() {
        return polymer.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    void setPolymer(final LinkedList<Character> polymer) {
        this.polymer = polymer;
    }

    void addReaction(final String pair, final char insertion) {
        reactions.put(pair, insertion);
    }

    void react() {
        final ListIterator<Character> elements = polymer.listIterator();

        while (elements.hasNext()) {
            final char one = elements.next();
            if (!elements.hasNext()) break;
            final char two = elements.next();
            elements.previous();

            final String pair = String.valueOf(one) + two;
            final Character insertion = reactions.get(pair);

            elements.add(insertion);
        }
    }

    void printlnPolymer(final String prefix) {
        System.out.printf("%s len = %5d: %s%n", prefix, polymer.size(),
                polymer.size() > 100 ? "(too big to print)" : getPolymer());
    }
}
