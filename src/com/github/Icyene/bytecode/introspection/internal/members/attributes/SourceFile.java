package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_UTF_STRING;

public class SourceFile extends Attribute {
    private Constant sourceIndex;
    private final ConstantPool owner;

    public SourceFile(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        sourceIndex = pool.get(stream.readShort());
        owner = pool;
    }

    public byte[] getBytes() {
        return Bytes.concat(super.getBytes(), Bytes.toByteArray((short)sourceIndex.getIndex()));
    }

    public String getSourceFile() {
        return sourceIndex.getStringValue();
    }

    public void setSourceFile(String newSource) {
        int source = sourceIndex.getIndex();
        owner.set(source, (sourceIndex = new Constant(source, TAG_UTF_STRING, newSource.getBytes(), owner)));
    }
}
