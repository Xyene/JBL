package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.Opcodes;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.LinkedList;

public class InstructionPool extends LinkedList<Opcodes> {

    public InstructionPool(ByteStream stream) {
        int size = stream.readInt();
        for (int i = 0; i != size; i++)
            add(Opcodes.getByValue(stream.readByte()));
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(size()));
        for (Opcodes code : this)
            out.write(code.getByte());
        return out.toByteArray();
    }
}
