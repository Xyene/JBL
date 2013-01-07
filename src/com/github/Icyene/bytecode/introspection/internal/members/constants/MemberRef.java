package com.github.Icyene.bytecode.introspection.internal.members.constants;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.metadata.readers.SignatureReader;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class MemberRef {
    private final String clazz;
    private final Descriptor descriptor;

    public MemberRef(ByteStream stream, ConstantPool pool) {
        this.clazz = pool.getString(stream.readShort());
        this.descriptor = pool.getDescriptor(stream.readShort());
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public String getOwningClass() {
        return clazz;
    }

    public String toString() {
        return clazz + "." + descriptor.toString();
    }
}
