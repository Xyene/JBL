package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_UTF_STRING;

public class SourceFile extends Attribute {
    private Constant sourceIndex;

    public SourceFile(ByteStream stream, Constant name, ConstantPool pool) {
        super(name, stream.readInt());
        sourceIndex = pool.get(stream.readShort());
    }

    public byte[] getBytes() {
        byte[] bytes = Bytes.toByteArray((short) sourceIndex.getIndex());
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    public String getSourceFile() {
        return sourceIndex.getStringValue();
    }

    public void setSourceFile(String newSource) {
        sourceIndex.getOwner().set(sourceIndex.getIndex(), (sourceIndex = new Constant(TAG_UTF_STRING, newSource.getBytes())));
    }
}
