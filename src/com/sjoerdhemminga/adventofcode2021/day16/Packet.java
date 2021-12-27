package com.sjoerdhemminga.adventofcode2021.day16;

import java.util.List;

record Packet(int version, int typeId, List<Packet> subpackets, long literalValue) {
}
