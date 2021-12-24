package com.sjoerdhemminga.adventofcode2021.day23.star2;

record SmallHall(SmallHallName name, char occupant) {
    boolean canEnter() {
        return occupant == '.';
    }

    SmallHall enter(final char shrimp) {
        if (!canEnter()) throw new AssertionError();
        return new SmallHall(name, shrimp);
    }

    boolean canPassThrough() {
        return canEnter();
    }

    boolean canLeave() {
        return occupant != '.';
    }

    char leaver() {
        if (!canLeave()) throw new AssertionError();
        return occupant;
    }

    SmallHall leave() {
        if (!canLeave()) throw new AssertionError();
        return new SmallHall(name, '.');
    }
}
