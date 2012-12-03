package com.github.Icyene.BytecodeStudio.Disassembler.Pools;

import com.github.Icyene.BytecodeStudio.Disassembler.Bytes;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Interface;

import java.util.LinkedList;

public class InterfacePool extends LinkedList<Interface> {
    private int offset = 16;

    public InterfacePool(byte[] clazz, ConstantPool pool) {
        offset += pool.getOffset() + 6;   //The access flags, super and this references
        short size = Bytes.readShort(clazz, offset);
        if (size <= 0) return;
        for (int i = 0; i != size; i++)
            add(new Interface(pool, Bytes.slice(clazz, offset, (offset += 2)), i));
    }

    public byte[] assemble() {
        byte[] raw = Bytes.getShort((short) size());
        for (Interface cpi : this)
            raw = Bytes.concat(raw, cpi.assemble());
        return raw;
    }

    public int getSizeInBytes() {
        return size() * 2; //Everything is u2
    }

    public int getOffset() {
        return offset;
    }
}
