package com.github.Icyene.BytecodeStudio.Disassembler.Indices;

import com.github.Icyene.BytecodeStudio.Disassembler.Pools.AttributePool;

public class MethodPoolIndex {

    private short accessFlags;
    private ConstantPoolIndex name;
    private ConstantPoolIndex descriptor;
    private AttributePool attributePool;

    public byte[] assemble() {
        return null;
    }

    public int getSizeInBytes() {
        return 6 + attributePool.size();
    }

    public short getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(short accessFlags) {
        this.accessFlags = accessFlags;
    }

    public ConstantPoolIndex getName() {
        return name;
    }

    public void setName(ConstantPoolIndex name) {
        this.name = name;
    }

    public ConstantPoolIndex getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(ConstantPoolIndex descriptor) {
        this.descriptor = descriptor;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}
