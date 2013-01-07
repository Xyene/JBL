package com.github.Icyene.bytecode.introspection.internal.members.constants;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class Descriptor {
    private final String name;
    private final String descriptor;

    public Descriptor(ByteStream stream, ConstantPool owner) {
        name = owner.getString(stream.readShort());
        descriptor = owner.getString(stream.readShort());
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String toString() {
        return name + ":" + descriptor;
    }
}
