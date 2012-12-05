package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.util.Bytes;
import com.github.Icyene.bytecode.disassembler.internal.pools.AttributePool;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;

class Field {

    private Flag accessFlags;
    private Constant name;
    private Constant descriptor;
    private AttributePool attributePool;

    Field(byte[] clazz, ConstantPool cpool, int offset) {
        accessFlags = new Flag(Bytes.toShort(clazz, offset));
        name = cpool.get(Bytes.toShort(clazz, offset + 2) - 1);
        descriptor = cpool.get(Bytes.toShort(clazz, offset + 4) - 1);
        int attributeSize = Bytes.toShort(clazz, offset + 6);
        offset += 8;
    }


    public byte[] assemble() {
        return null;
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