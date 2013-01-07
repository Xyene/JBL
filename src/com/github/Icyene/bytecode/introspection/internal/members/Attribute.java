package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public class Attribute {

    protected int length;
    protected Constant name;

    public Attribute(ByteStream stream, Constant name, ConstantPool pool) {
        this.name = name;
        this.length = stream.readInt();
    }

    public Attribute(Constant name, int length) {
        this.name = name;
        this.length = length;
    }

    public Attribute() {
    }

    public byte[] getBytes() {
        return Bytes.concat(Bytes.toByteArray((short) name.getIndex()), Bytes.toByteArray(length));
    }

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }

    public final int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }
}