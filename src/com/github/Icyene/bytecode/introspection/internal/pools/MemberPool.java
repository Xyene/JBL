package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

public class MemberPool<E extends Member> extends LinkedList<E> {
    protected ClassFile owner;

    public MemberPool(ByteStream stream, ConstantPool pool, ClassFile owner) {
        this.owner = owner;
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(make(stream, pool));
    }

    public MemberPool() {}

    protected E make(ByteStream stream, ConstantPool pool) {
        return (E)new Member(stream, pool);
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) size()));
        for (Member p : this)
            out.write(p.getBytes());
        return out.toByteArray();
    }

    public ClassFile getOwner() {
        return owner;
    }
}
