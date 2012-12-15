package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.AccessFlag;
import com.github.Icyene.bytecode.disassembler.internal.pools.AttributePool;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class Property {
    private AccessFlag accessFlags;
    private Constant name;
    private Constant descriptor;
    private AttributePool attributePool;

    public Property(ByteStream stream, ConstantPool pool) {
        accessFlags = new AccessFlag(stream.readShort());
        name = pool.get(stream.readShort() );
        descriptor = pool.get(stream.readShort() );
        attributePool = new AttributePool(stream, pool);
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(accessFlags.getBytes());
        out.write(Bytes.toByteArray((short) name.getIndex()));
        out.write(Bytes.toByteArray((short) descriptor.getIndex()));
        out.write(attributePool.getBytes());
        return out.toByteArray();
    }

    public int getSizeInBytes() {
        return 6 + attributePool.size();
    }

    public AccessFlag getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(AccessFlag accessFlags) {
        this.accessFlags = accessFlags;
    }

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }

    public Constant getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Constant descriptor) {
        this.descriptor = descriptor;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}
