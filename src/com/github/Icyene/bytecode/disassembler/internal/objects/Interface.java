package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class Interface {

    private Constant classReference;
    private Constant descriptor;

    public Interface(ConstantPool pool, byte[] value) {
        this.classReference = pool.get(Bytes.toShort(value, 0));
        this.descriptor = pool.get(Bytes.toShort(value, 2));
    }

    public byte[] getBytes() {
        return Bytes.concat(classReference.getBytes(), classReference.getBytes());
    }

    public int getSizeInBytes() {
        return 2;
    }

    public Constant getClassReference() {
        return classReference;
    }

    public void setClassReference(Constant classReference) {
        this.classReference = classReference;
    }

    public Constant getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Constant descriptor) {
        this.descriptor = descriptor;
    }

    public String prettyPrint() {
        return "[C=" + classReference.getStringValue() + ", D=" + descriptor.getStringValue() + "]";
    }
}
