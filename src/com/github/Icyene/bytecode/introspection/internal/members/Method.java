package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class Method extends Member {

    public Method(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
    }
}