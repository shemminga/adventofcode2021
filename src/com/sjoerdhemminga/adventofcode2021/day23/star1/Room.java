package com.sjoerdhemminga.adventofcode2021.day23.star1;

record Room (RoomName name, char shallow, char deep) {
    boolean canEnter(final char shrimp) {
        final boolean allowedForRoom = !name.hasTypeLimit() || shrimp == name.typeLimit();
        final boolean noWeirdosInMyRoom = !name.hasTypeLimit() || deep == '.' || deep == shrimp;
        final boolean entranceFree = shallow == '.';
        return allowedForRoom && noWeirdosInMyRoom && entranceFree;
    }

    boolean canEnterDeep(final char shrimp) {
        return canEnter(shrimp) && deep == '.';
    }

    Room enterDeep(final char shrimp) {
        if (!canEnterDeep(shrimp)) throw new AssertionError();
        return new Room(name, shallow, shrimp);
    }

    Room enterShallow(final char shrimp) {
        if (!canEnter(shrimp)) throw new AssertionError();
        return new Room(name, shrimp, deep);
    }

    boolean canLeave() {
        return shallow != '.' || deep != '.';
    }

    char leaver() {
        if (!canLeave()) throw new AssertionError();
        return shallow != '.' ? shallow : deep;
    }

    Room leave() {
        if (!canLeave()) throw new AssertionError();
        return new Room(name, '.', shallow == '.' ? '.' : deep);
    }

    int leaveSteps() {
        if (!canLeave()) throw new AssertionError();
        return shallow == '.' ? 2 : 1;
    }

    boolean isRealRoom() {
        return name.hasTypeLimit();
    }

    boolean inEndState() {
        return shallow == name.typeLimit() && deep == name.typeLimit();
    }
}
