package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_INTEGER;

public class ConstantValue extends Attribute {
    private Constant constantIndex;

    public ConstantValue(ByteStream stream, Constant name, ConstantPool pool) {
        super(name, stream.readInt());
        constantIndex = pool.get(stream.readShort());
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(super.getBytes());
        out.write(Bytes.toByteArray((short) constantIndex.getIndex()));
        byte[] bytes = out.toByteArray();
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    public int getConstantIndex() {
        return constantIndex.getIndex();
    }

    public void setConstantIndex(int index) {
        constantIndex.getOwner().set(constantIndex.getIndex(), (constantIndex = new Constant(TAG_INTEGER, Bytes.toByteArray((short) index))));
    }
}
