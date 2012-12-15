package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class ConstantValueAttribute extends Attribute {
    private Constant constantIndex;

    public ConstantValueAttribute(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        constantIndex = pool.get(stream.readShort());
    }

    public Constant getConstantIndex() {
        return constantIndex;
    }

    public void setConstantIndex(Constant constantIndex) {
        this.constantIndex = constantIndex;
    }
}
