package com.sjoerdhemminga.adventofcode2021.day23.star1;

import com.sjoerdhemminga.adventofcode2021.day23.star1.Layout.RoomToRoom;
import com.sjoerdhemminga.adventofcode2021.day23.star1.Layout.RoomToSmallHall;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2021.day23.star1.Layout.RTR;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.Layout.RTSH;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.Layout.SHTR;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.Layout.SmallHallToRoom;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.RoomName.A_HALL;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.RoomName.A_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.RoomName.B_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.RoomName.C_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.RoomName.D_HALL;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.RoomName.D_ROOM;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.SmallHallName.AB;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.SmallHallName.BC;
import static com.sjoerdhemminga.adventofcode2021.day23.star1.SmallHallName.CD;
import static java.util.Map.Entry;
import static java.util.Map.entry;
import static java.util.function.Function.identity;

record Burrow(Map<RoomName, Room> rooms, Map<SmallHallName, SmallHall> smallHalls, int expendedEnergy) {
    static Burrow ofString(final String topSpots, final String bottomSpots) {
        final Map<RoomName, Room> rooms = Stream.of(
                        new Room(A_HALL, '.', '.'),
                        new Room(A_ROOM, topSpots.charAt(0), bottomSpots.charAt(0)),
                        new Room(B_ROOM, topSpots.charAt(1), bottomSpots.charAt(1)),
                        new Room(C_ROOM, topSpots.charAt(2), bottomSpots.charAt(2)),
                        new Room(D_ROOM, topSpots.charAt(3), bottomSpots.charAt(3)),
                        new Room(D_HALL, '.', '.'))
                .collect(Collectors.toUnmodifiableMap(Room::name, identity()));

        final Map<SmallHallName, SmallHall> smallHalls = Stream.of(
                        new SmallHall(AB, '.'),
                        new SmallHall(BC, '.'),
                        new SmallHall(CD, '.'))
                .collect(Collectors.toUnmodifiableMap(SmallHall::name, identity()));

        return new Burrow(rooms, smallHalls, 0);
    }

    Set<Burrow> nextStates() {
        final Set<Burrow> nextStates = new HashSet<>();

        smallHalls.values()
                .stream()
                .filter(SmallHall::canLeave)
                .flatMap(from -> rooms.values()
                        .stream()
                        .filter(Room::isRealRoom) // Cannot move from hall to hall
                        .filter(to -> to.canEnter(from.leaver()))
                        .flatMap(to -> move(from, to)))
                .forEach(nextStates::add);

        smallHalls.values()
                .stream()
                .filter(SmallHall::canEnter)
                .flatMap(to -> rooms.values()
                        .stream()
                        .filter(Room::isRealRoom) // Cannot move from hall to hall
                        .filter(Room::canLeave)
                        .flatMap(from -> move(from, to)))
                .forEach(nextStates::add);

        rooms.values()
                .stream()
                .filter(Room::canLeave)
                .flatMap(from -> rooms.values()
                        .stream()
                        .filter(to -> from.isRealRoom() || to.isRealRoom())
                        .filter(to -> to.canEnter(from.leaver()))
                        .flatMap(to -> move(from, to)))
                .forEach(nextStates::add);

        return nextStates;
    }

    private Stream<Burrow> move(final Room from, final Room to) {
        final List<SmallHall> intermediates = RTR.stream()
                .filter(rtr -> rtr.from() == from.name())
                .filter(rtr -> rtr.to() == to.name())
                .map(RoomToRoom::intermediates)
                .flatMap(Arrays::stream)
                .map(smallHalls::get)
                .toList();

        final boolean clearRoute = intermediates.stream()
                .allMatch(SmallHall::canPassThrough);

        if (!clearRoute) return Stream.empty();

        final Room leftRoom = from.leave();
        final int baseSteps = from.leaveSteps() + intermediates.size() * 2 + 1;

        final Room shallowEnteredRoom = to.enterShallow(from.leaver());
        final int shallowCost = moveCost(from.leaver(), baseSteps);
        final Burrow shallowMove = newBurrow(shallowEnteredRoom, leftRoom, shallowCost);

        if (!to.canEnterDeep(from.leaver())) return Stream.of(shallowMove);

        final Room deepEnteredRoom = to.enterDeep(from.leaver());
        final int deepCost = moveCost(from.leaver(), baseSteps + 1);
        final Burrow deepMove = newBurrow(deepEnteredRoom, leftRoom, deepCost);

        return Stream.of(shallowMove, deepMove);
    }

    private Stream<Burrow> move(final Room from, final SmallHall to) {
        final List<SmallHall> intermediates = RTSH.stream()
                .filter(rtsh -> rtsh.from() == from.name())
                .filter(rtsh -> rtsh.to() == to.name())
                .map(RoomToSmallHall::intermediates)
                .flatMap(Arrays::stream)
                .map(smallHalls::get)
                .toList();

        final boolean clearRoute = intermediates.stream()
                .allMatch(SmallHall::canPassThrough);

        if (!clearRoute) return Stream.empty();

        final Room leftRoom = from.leave();
        final SmallHall enteredHall = to.enter(from.leaver());
        final int steps = from.leaveSteps() + intermediates.size() * 2 + 1;

        final Burrow move = newBurrow(leftRoom, enteredHall, moveCost(from.leaver(), steps));

        return Stream.of(move);
    }

    private Stream<Burrow> move(final SmallHall from, final Room to) {
        final List<SmallHall> intermediates = SHTR.stream()
                .filter(shtr -> shtr.from() == from.name())
                .filter(shtr -> shtr.to() == to.name())
                .map(SmallHallToRoom::intermediates)
                .flatMap(Arrays::stream)
                .map(smallHalls::get)
                .toList();

        final boolean clearRoute = intermediates.stream()
                .allMatch(SmallHall::canPassThrough);

        if (!clearRoute) return Stream.empty();

        final SmallHall leftHall = from.leave();
        final int baseSteps = 1 + intermediates.size() * 2 + 1; // 1 out, 1 in.

        final Room shallowEnteredRoom = to.enterShallow(from.leaver());
        final int shallowCost = moveCost(from.leaver(), baseSteps);
        final Burrow shallowMove = newBurrow(shallowEnteredRoom, leftHall, shallowCost);

        if (!to.canEnterDeep(from.leaver())) return Stream.of(shallowMove);

        final Room deepEnteredRoom = to.enterDeep(from.leaver());
        final int deepCost = moveCost(from.leaver(), baseSteps + 1);
        final Burrow deepMove = newBurrow(deepEnteredRoom, leftHall, deepCost);

        return Stream.of(shallowMove, deepMove);
    }

    private Burrow newBurrow(final Room changedRoom, final SmallHall changedHall, final int cost) {
        final Map<RoomName, Room> newRooms = copyReplace(rooms, changedRoom.name(), changedRoom);
        final Map<SmallHallName, SmallHall> newSmallHalls = copyReplace(smallHalls, changedHall.name(), changedHall);
        return new Burrow(newRooms, newSmallHalls, expendedEnergy + cost);
    }

    private Burrow newBurrow(final Room changedRoom1, final Room changedRoom2, final int cost) {
        final Map<RoomName, Room> newRooms1 = copyReplace(rooms, changedRoom1.name(), changedRoom1);
        final Map<RoomName, Room> newRooms2 = copyReplace(newRooms1, changedRoom2.name(), changedRoom2);
        return new Burrow(newRooms2, smallHalls, expendedEnergy + cost);
    }

    boolean inEndState() {
        return rooms.values()
                .stream()
                .allMatch(Room::inEndState);
    }

    private static int moveCost(final char type, final int steps) {
        return switch (type) {
            case 'A' -> steps;
            case 'B' -> steps * 10;
            case 'C' -> steps * 100;
            case 'D' -> steps * 1000;
            default -> throw new IllegalArgumentException();
        };
    }

    private static <K, V> Map<K, V> copyReplace(final Map<K, V> map, final K key, final V value) {
        return map.entrySet()
                .stream()
                .map(e -> e.getKey()
                        .equals(key) ? entry(key, value) : e)
                .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
    }

    Burrow withoutEnergy() {
        return new Burrow(rooms, smallHalls, 0);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        return new StringBuilder()
                .append("+-----------------------------------------\n")
                .append("| #############  expended energy: ")
                .append(expendedEnergy)
                .append("\n")
                .append("| #")
                .append(rooms.get(A_HALL).deep())
                .append(rooms.get(A_HALL).shallow())
                .append('.')
                .append(smallHalls.get(AB).occupant())
                .append('.')
                .append(smallHalls.get(BC).occupant())
                .append('.')
                .append(smallHalls.get(CD).occupant())
                .append('.')
                .append(rooms.get(D_HALL).shallow())
                .append(rooms.get(D_HALL).deep())
                .append("#\n")
                .append("| ###")
                .append(rooms.get(A_ROOM).shallow())
                .append('|')
                .append(rooms.get(B_ROOM).shallow())
                .append('|')
                .append(rooms.get(C_ROOM).shallow())
                .append('|')
                .append(rooms.get(D_ROOM).shallow())
                .append("###\n")
                .append("|   #")
                .append(rooms.get(A_ROOM).deep())
                .append('|')
                .append(rooms.get(B_ROOM).deep())
                .append('|')
                .append(rooms.get(C_ROOM).deep())
                .append('|')
                .append(rooms.get(D_ROOM).deep())
                .append("#\n")
                .append("|   #########\n")
                .toString();
    }
}
