package com.github.Icyene.BytecodeStudio.Disassembler.Indices;

import com.github.Icyene.BytecodeStudio.Disassembler.Pools.AttributePool;

public class Method {

    private short accessFlags;
    private Constant name;
    private Constant descriptor;
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
