package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.ClassFile;
import com.github.Icyene.bytecode.introspection.internal.Member;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

public class MemberPool extends LinkedList<Member> {

    /**
     * Constructs a member pool.
     *
     * @param stream The stream of bytes containing the pool data.
     * @param pool   An associated constant pool.
     */
    public MemberPool(ByteStream stream, ConstantPool pool, ClassFile owner) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new Member(stream, pool));
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public MemberPool() {
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) size()));
        for (Member p : this)
            out.write(p.getBytes());
        return out.toByteArray();
    }
}
