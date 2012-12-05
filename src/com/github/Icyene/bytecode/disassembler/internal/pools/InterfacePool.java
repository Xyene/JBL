package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.objects.Interface;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.LinkedList;

public class InterfacePool extends LinkedList<Interface> {
    public InterfacePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();
        if (size <= 0) return;
        for (int i = 0; i != size; i++)
            add(new Interface(pool, stream.read(4), i));
    }

    public byte[] assemble() {
        byte[] raw = Bytes.toByteArray((short) size());
        for (Interface cpi : this)
            raw = Bytes.concat(raw, cpi.assemble());
        return raw;
    }

    public int getSizeInBytes() {
        return size() * 2; //Everything is u2
    }
}
