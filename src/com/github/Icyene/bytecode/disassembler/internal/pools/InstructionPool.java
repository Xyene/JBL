package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.Opcodes;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.LinkedList;

public class InstructionPool extends LinkedList<Opcodes> {

    public InstructionPool(ByteStream stream) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(Opcodes.getByValue(stream.readByte()));
    }

    public byte[] assemble() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) size()));
        for (Opcodes code : this)
            out.write(code.getByte());
        return out.toByteArray();
    }
}
