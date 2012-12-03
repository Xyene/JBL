package com.github.Icyene.BytecodeStudio.Disassembler.Pools;

import com.github.Icyene.BytecodeStudio.Disassembler.Bytes;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Attribute;

import java.util.LinkedList;

public class AttributePool extends LinkedList<Attribute> {

    public byte[] assemble() {
        byte[] ret = Bytes.getShort((short) size());
        for (Attribute a : this)
            ret = Bytes.concat(ret, a.assemble());
        return ret;
    }
}
