package com.sjoerdhemminga.adventofcode2021.day23.star2;

import java.util.List;

import static com.sjoerdhemminga.adventofcode2021.day23.star2.RoomName.A_HALL;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.RoomName.A_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.RoomName.B_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.RoomName.C_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.RoomName.D_HALL;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.RoomName.D_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.SmallHallName.AB;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.SmallHallName.BC;
import static com.sjoerdhemminga.adventofcode2021.day23.star2.SmallHallName.CD;

final class Layout {
    static final List<RoomToRoom> RTR = List.of(
            new RoomToRoom(A_HALL, A_ROOM),
            new RoomToRoom(A_HALL, B_ROOM, AB),
            new RoomToRoom(A_HALL, C_ROOM, AB, BC),
            new RoomToRoom(A_HALL, D_ROOM, AB, BC, CD),
            // A_HALL -> D_HALL not possible

            new RoomToRoom(A_ROOM, A_HALL),
            new RoomToRoom(A_ROOM, B_ROOM, AB),
            new RoomToRoom(A_ROOM, C_ROOM, AB, BC),
            new RoomToRoom(A_ROOM, D_ROOM, AB, BC, CD),
            new RoomToRoom(A_ROOM, D_HALL, AB, BC, CD),

            new RoomToRoom(B_ROOM, A_HALL, AB),
            new RoomToRoom(B_ROOM, A_ROOM, AB),
            new RoomToRoom(B_ROOM, C_ROOM, BC),
            new RoomToRoom(B_ROOM, D_ROOM, BC, CD),
            new RoomToRoom(B_ROOM, D_HALL, BC, CD),

            new RoomToRoom(C_ROOM, A_HALL, AB, BC),
            new RoomToRoom(C_ROOM, A_ROOM, AB, BC),
            new RoomToRoom(C_ROOM, B_ROOM, BC),
            new RoomToRoom(C_ROOM, D_ROOM, CD),
            new RoomToRoom(C_ROOM, D_HALL, CD),

            new RoomToRoom(D_ROOM, A_HALL, AB, BC, CD),
            new RoomToRoom(D_ROOM, A_ROOM, AB, BC, CD),
            new RoomToRoom(D_ROOM, B_ROOM, BC, CD),
            new RoomToRoom(D_ROOM, C_ROOM, CD),
            new RoomToRoom(D_ROOM, D_HALL),

            // D_HALL -> A_HALL not possible
            new RoomToRoom(D_HALL, A_ROOM, AB, BC, CD),
            new RoomToRoom(D_HALL, B_ROOM, BC, CD),
            new RoomToRoom(D_HALL, C_ROOM, CD),
            new RoomToRoom(D_HALL, D_ROOM));

    static final List<RoomToSmallHall> RTSH = List.of(
            new RoomToSmallHall(A_ROOM, AB),
            new RoomToSmallHall(A_ROOM, BC, AB),
            new RoomToSmallHall(A_ROOM, CD, AB, BC),

            new RoomToSmallHall(B_ROOM, AB),
            new RoomToSmallHall(B_ROOM, BC),
            new RoomToSmallHall(B_ROOM, CD, BC),

            new RoomToSmallHall(C_ROOM, AB, BC),
            new RoomToSmallHall(C_ROOM, BC),
            new RoomToSmallHall(C_ROOM, CD),

            new RoomToSmallHall(D_ROOM, AB, BC, CD),
            new RoomToSmallHall(D_ROOM, BC, CD),
            new RoomToSmallHall(D_ROOM, CD));

    static final List<SmallHallToRoom> SHTR = RTSH.stream()
            .map(rtsh -> new SmallHallToRoom(rtsh.to(), rtsh.from(), rtsh.intermediates()))
            .toList();

    record RoomToRoom(RoomName from, RoomName to, SmallHallName... intermediates) {
    }

    record RoomToSmallHall(RoomName from, SmallHallName to, SmallHallName... intermediates) {
    }

    record SmallHallToRoom(SmallHallName from, RoomName to, SmallHallName... intermediates) {
    }
}
