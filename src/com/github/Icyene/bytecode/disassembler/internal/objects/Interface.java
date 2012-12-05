package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.util.Bytes;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;

public class Interface {

    private Constant classReference;
    private Constant descriptor;
    private final int index;

    public Interface(ConstantPool pool, byte[] value, int index) {
        this.classReference = pool.get(Bytes.toShort(value, 0));
        this.descriptor = pool.get(Bytes.toShort(value, 2));
        this.index = index;
    }

    public byte[] assemble() {
        return Bytes.concat(classReference.assemble(), classReference.assemble());
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
        return "[C=" + classReference.prettyPrint() + ", D=" + descriptor.prettyPrint() + "]";
    }
}
