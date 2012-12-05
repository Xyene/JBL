package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;

public abstract class Attribute {

    protected Constant name;
    protected int length;

    public Attribute(ByteStream stream, ConstantPool pool) {
        this.name = pool.get(stream.readShort() - 1);
        this.length = stream.readInt();
    }

    public Attribute(int length, Constant name) {

    }

    public byte[] assemble() {
        return Bytes.concat(Bytes.toByteArray((short) name.getIndex()), Bytes.toByteArray(length));
    }

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}