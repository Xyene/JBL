package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.attributes.IgnoredAttribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.LinkedList;

public class AttributePool extends LinkedList<Attribute> {

    public AttributePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();

        for (int i = 0; i != size; i++) {
            add(new IgnoredAttribute(stream, pool));
        }
    }

    public byte[] assemble() {
        byte[] ret = Bytes.toByteArray((short) size());
        for (Attribute a : this)
            ret = Bytes.concat(ret, a.assemble());
        return ret;
    }
}
