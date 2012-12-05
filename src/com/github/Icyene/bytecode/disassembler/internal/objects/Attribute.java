package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public abstract class Attribute {

    protected Constant name;
    protected int length;

    public Attribute(ByteStream stream, ConstantPool pool) {
        short index = stream.readShort();
        this.name = pool.get(index);
        System.out.println(">Creating attribute with cpool index: " + index + "(" + name.prettyPrint() + ")");
        this.length = stream.readInt();
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