package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_UTF_STRING;

public class Interface {

    private Constant classReference;
    private final ConstantPool owner;

    public Interface(ConstantPool pool, Constant value) {
        System.out.println("New interface: " + classReference);
        classReference = value;
        owner = pool;
    }

    public byte[] getBytes() {
        return Bytes.toByteArray((short) classReference.getIndex());
    }

    public String getClassReference() {
        return classReference.getStringValue();
    }

    public void setClassReference(String newRef) {
        int source = classReference.getIndex();
        owner.set(source, (classReference = new Constant(source, TAG_UTF_STRING, newRef.getBytes(), owner)));
    }
}
