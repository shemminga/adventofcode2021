package com.sjoerdhemminga.adventofcode2021.day23.star1;

enum RoomName {
    A_HALL('.'),
    A_ROOM('A'),
    B_ROOM('B'),
    C_ROOM('C'),
    D_ROOM('D'),
    D_HALL('.');

    private final char typeLimit;
    RoomName(final char typeLimit) {
        this.typeLimit = typeLimit;
    }

    char typeLimit() {
        return typeLimit;
    }

    boolean hasTypeLimit() {
        return typeLimit != '.';
    }
}
