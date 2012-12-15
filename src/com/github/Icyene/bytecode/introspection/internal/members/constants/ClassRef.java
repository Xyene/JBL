package com.github.Icyene.bytecode.introspection.internal.members.constants;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.metadata.Tag;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;

public class ClassRef extends Constant {
    public ClassRef(int index, Tag type, byte[] value, ConstantPool owner) {
        super(index, type, value, owner);
    }
}
