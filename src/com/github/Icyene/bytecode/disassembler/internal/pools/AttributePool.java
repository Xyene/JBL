package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;
import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;

import java.util.LinkedList;

public class AttributePool extends LinkedList<Attribute> {

    public AttributePool(ByteStream stream) {
    }

    public byte[] assemble() {
        byte[] ret = Bytes.toByteArray((short) size());
        for (Attribute a : this)
            ret = Bytes.concat(ret, a.assemble());
        return ret;
    }
}
