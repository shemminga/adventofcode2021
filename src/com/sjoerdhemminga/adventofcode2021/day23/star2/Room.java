package com.sjoerdhemminga.adventofcode2021.day23.star2;

record Room (RoomName name, char shallow, char deep1, char deep2, char deep3) {
    boolean canEnter(final char shrimp) {
        final boolean weirdosInTheDeep =
                (deep1 != '.' && deep1 != shrimp) ||
                (deep2 != '.' && deep2 != shrimp) ||
                (deep3 != '.' && deep3 != shrimp);

        final boolean allowedForRoom = !name.hasTypeLimit() || shrimp == name.typeLimit();
        final boolean noWeirdosInMyRoom = !name.hasTypeLimit() || !weirdosInTheDeep;
        final boolean entranceFree = shallow == '.';
        return allowedForRoom && noWeirdosInMyRoom && entranceFree;
    }

    boolean canEnterDeep(final char shrimp) {
        return canEnter(shrimp) && (deep1 == '.' || deep2 == '.' || deep3 == '.');
    }

    Room enterDeep(final char shrimp) {
        if (!canEnterDeep(shrimp)) throw new AssertionError();

        if (deep1 == '.' && deep2 == '.' && deep3 == '.') return new Room(name, shallow, '.', '.', shrimp);
        if (deep1 == '.' && deep2 == '.') return new Room(name, shallow, '.', shrimp, deep3);

        return new Room(name, shallow, shrimp, deep2, deep3);
    }

    Room enterShallow(final char shrimp) {
        if (!canEnter(shrimp)) throw new AssertionError();
        return new Room(name, shrimp, deep1, deep2, deep3);
    }

    boolean canLeave() {
        return shallow != '.' || deep1 != '.' ||
                (deep2 != '.' && deep2 != 'X') ||
                (deep3 != '.' && deep3 != 'X');
    }

    char leaver() {
        if (!canLeave()) throw new AssertionError();
        if (shallow != '.') return shallow;
        if (deep1 != '.') return deep1;
        if (deep2 != '.') return deep2;
        return deep3;
    }

    Room leave() {
        if (!canLeave()) throw new AssertionError();
        if (shallow != '.') return new Room(name, '.', deep1, deep2, deep3);
        if (deep1 != '.') return new Room(name, '.', '.', deep2, deep3);
        if (deep2 != '.') return new Room(name, '.', '.', '.', deep3);
        return new Room(name, '.', '.', '.', '.');
    }

    int leaveSteps() {
        if (!canLeave()) throw new AssertionError();
        if (shallow == '.' && deep1 == '.' && deep2 == '.') return 4;
        if (shallow == '.' && deep1 == '.') return 3;
        if (shallow == '.') return 2;
        return 1;
    }

    int shallowEnterSteps() {
        if (shallow != '.') throw new AssertionError();
        return 1;
    }

    int deepEnterSteps() {
        if (shallow == '.' && deep1 == '.' && deep2 == '.' && deep3 == '.') return 4;
        if (shallow == '.' && deep1 == '.' && deep2 == '.') return 3;
        if (shallow == '.' && deep1 == '.') return 2;
        throw new AssertionError();
    }

    boolean isRealRoom() {
        return name.hasTypeLimit();
    }

    boolean inEndState() {
        if (name.hasTypeLimit())
            return shallow == name.typeLimit() &&
                    deep1 == name.typeLimit() &&
                    deep2 == name.typeLimit() &&
                    deep3 == name.typeLimit();

        return shallow == '.' && deep1 == '.' && deep2 == 'X' && deep3 == 'X';
    }
}
