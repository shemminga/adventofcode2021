package com.sjoerdhemminga.adventofcode2021.day16;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

final class Parser {
    static Packet parse(final String data) {
        final ParseResult parseResult = parseMultiplePackets(data, 1);
        if (parseResult.packets.size() != 1) throw new AssertionError();
        return parseResult.packets.get(0);
    }

    private static ParseResult parseMultiplePackets(final String data, final int maxPackets) {
        int curr = 0;
        final List<Packet> packets = new ArrayList<>();

        for (int i = 0; i < maxPackets; i++) {
            final String remainingData = data.substring(curr);
            final ParseResult parseResult = parseOnePacket(remainingData);
            curr += parseResult.consumedBits();
            packets.addAll(parseResult.packets);
        }

        return new ParseResult(curr, packets);
    }

    private static ParseResult parseMultiplePackets(final String data) {
        int curr = 0;
        final List<Packet> packets = new ArrayList<>();

        while (curr < data.length()) {
            final String remainingData = data.substring(curr);
            final ParseResult parseResult = parseOnePacket(remainingData);
            curr += parseResult.consumedBits();
            packets.addAll(parseResult.packets);
        }

        return new ParseResult(data.length(), packets);
    }

    private static ParseResult parseOnePacket(final String data) {
        final int version = parseInt(data.substring(0, 3), 2);
        final int typeId = parseInt(data.substring(3, 6), 2);
        final String payload = data.substring(6);

        // Literal value
        if (typeId == 4) {
            final ParseResultLiteral parseResultLiteral = parseLiterals(payload);
            final Packet packet = new Packet(version, typeId, List.of(), parseResultLiteral.parsedLiteral());
            return new ParseResult(parseResultLiteral.consumedBits() + 6, List.of(packet));
        }

        // Counted bits in subpackets
        if (payload.charAt(0) == '0') {
            final int bitCount = parseInt(payload.substring(1, 16), 2);
            final String subpacketData = payload.substring(16, 16 + bitCount);
            final ParseResult parseResult = parseMultiplePackets(subpacketData);
            final Packet packet = new Packet(version, typeId, parseResult.packets(), 0);
            return new ParseResult(22 + bitCount, List.of(packet));
        }

        // Counted subpackets
        final int packetCount = parseInt(payload.substring(1, 12), 2);
        final String subpacketData = payload.substring(12);
        final ParseResult parseResult = parseMultiplePackets(subpacketData, packetCount);
        final Packet packet = new Packet(version, typeId, parseResult.packets(), 0);
        return new ParseResult(18 + parseResult.consumedBits(), List.of(packet));
    }

    private static ParseResultLiteral parseLiterals(final String data) {
        int curr = 0;
        final StringBuilder currVal = new StringBuilder();

        // Non-last blocks start with 1
        while (data.charAt(curr) == '1') {
            currVal.append(data, curr + 1, curr + 5);
            curr += 5;
        }

        // Last block starts with 0
        currVal.append(data, curr + 1, curr + 5);
        curr += 5;

        return new ParseResultLiteral(curr, parseLong(currVal.toString(), 2));
    }

    private record ParseResultLiteral(int consumedBits, long parsedLiteral) {}

    private record ParseResult(int consumedBits, List<Packet> packets) {}
}

// Header:
// First 3 bits: packet version
// Next 3 bits: type ID

// Type IDs:
// 100 4: literal value
// others: operator packets

// literal value:
// chunks of 5 bits:
// - first bit 0 for last chunk, 1 otherwise

// operator packets:
// 1 bit: length type ID
// length type ID 0:
// - 15 bits representing count of bits in sub-packets
// length type ID 1:
// - 11 bits representing count of sub-packets
// subpackets
