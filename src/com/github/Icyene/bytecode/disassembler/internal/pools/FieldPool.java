package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.objects.Field;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.LinkedList;

public class FieldPool extends LinkedList<Field> {
    public FieldPool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new Field(stream, pool));
    }

    public byte[] assemble() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) size()));
        for (Field f : this)
            out.write(f.assemble());
        return out.toByteArray();
    }
}

