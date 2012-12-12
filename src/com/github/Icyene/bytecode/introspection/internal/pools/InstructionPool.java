package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.Opcode;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

public class InstructionPool extends LinkedList<Opcode> {

    public InstructionPool(ByteStream stream) {
        int size = stream.readInt();
        for (int i = 0; i != size; i++)
            add(Opcode.getByValue(stream.readByte()));
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(size()));
        for (Opcode code : this)
            out.write(code.getByte());
        return out.toByteArray();
    }
}
