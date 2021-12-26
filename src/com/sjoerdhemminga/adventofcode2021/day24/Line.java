package com.sjoerdhemminga.adventofcode2021.day24;

record Line(Instruction instruction, Register paramA, boolean hasParamB, Register paramBreg, int paramBint) {
    @Override
    public String toString() {
        final String paramBStr;
        if (!hasParamB) paramBStr = " ";
        else if(paramBreg != null) paramBStr = " " + paramBreg;
        else paramBStr = " " + paramBint;

        return instruction + " " + paramA + paramBStr;
    }
}
