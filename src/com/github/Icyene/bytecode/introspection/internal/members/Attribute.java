package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public abstract class Attribute {

    protected int length;
    protected Constant name;

    public Attribute(ByteStream stream, Constant name, ConstantPool pool) {
        this.name = name;
        //System.out.println(">>> Creating attribute with cpool index: " + index + "(" + name.prettyPrint() + ")");
        this.length = stream.readInt();
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