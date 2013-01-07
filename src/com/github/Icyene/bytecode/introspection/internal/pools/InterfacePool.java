package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.members.Interface;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

public class InterfacePool extends LinkedList<Interface> {
    public InterfacePool(ByteStream stream, ConstantPool pool) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new Interface(pool, pool.get(Bytes.toShort(stream.read(2), 0))));
    }

    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) size());
        for (Interface cpi : this)
            raw = Bytes.concat(raw, cpi.getBytes());
        return raw;
    }

    public boolean contains(String s) {
        for(Interface c: this)
            if(c.getClassReference().equals(s))
                return true;
        return false;
    }
}
