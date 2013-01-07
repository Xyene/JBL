package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.internal.pools.ExceptionPool;
import com.github.Icyene.bytecode.introspection.internal.pools.InstructionPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public class Code extends Attribute {

    private int maxStack;
    private int maxLocals;
    private byte[] codePool;
    private ExceptionPool exceptionPool;
    private AttributePool attributePool;

    public Code(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        maxStack = stream.readShort();
        maxLocals = stream.readShort();
        codePool = stream.read(stream.readInt());
        exceptionPool = new ExceptionPool(stream);
        attributePool = new AttributePool(stream, pool);
    }

    public Code() {

    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        //out.write(super.getBytes());
        out.write((short)maxStack);
        out.write((short)maxLocals);
        out.write(Bytes.toByteArray(codePool.length));
        out.write(codePool);
        out.write(exceptionPool.getBytes());
        out.write(attributePool.getBytes());
        byte[] bytes = out.toByteArray();
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public byte[] getCodePool() {
        return codePool;
    }

    public void setCodePool(byte[] codePool) {
        this.codePool = codePool;
    }

    public ExceptionPool getExceptionPool() {
        return exceptionPool;
    }

    public void setExceptionPool(ExceptionPool exceptionPool) {
        this.exceptionPool = exceptionPool;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}
