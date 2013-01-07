package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public class ConstantValue extends Attribute {
    private Constant constantIndex;

    public ConstantValue(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        constantIndex = pool.get(stream.readShort());
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(super.getBytes());
        out.write(Bytes.toByteArray((short)constantIndex.getIndex()));
        return out.toByteArray();
    }

    public Constant getConstantIndex() {
        return constantIndex;
    }

    public void setConstantIndex(Constant constantIndex) {
        this.constantIndex = constantIndex;
    }
}
