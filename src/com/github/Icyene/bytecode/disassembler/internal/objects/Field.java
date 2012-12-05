package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.Flag;
import com.github.Icyene.bytecode.disassembler.internal.pools.AttributePool;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class Field {

    private Flag accessFlags;
    private Constant name;
    private Constant descriptor;
    private AttributePool attributePool;

    public Field(ByteStream stream, ConstantPool pool) {
        accessFlags = new Flag(stream.readShort());
        name = pool.get(stream.readShort());
        descriptor = pool.get(stream.readShort());
        attributePool = new AttributePool(stream, pool);
    }


    public byte[] assemble() {
        ByteStream out = new ByteStream();
        out.write(accessFlags.assemble());
        out.write(Bytes.toByteArray((short) name.getIndex()));
        out.write(Bytes.toByteArray((short) descriptor.getIndex()));
        out.write(attributePool.assemble());
        return out.toByteArray();
    }

    public int getSizeInBytes() {
        return 6 + attributePool.size();
    }

    public Flag getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(Flag accessFlags) {
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