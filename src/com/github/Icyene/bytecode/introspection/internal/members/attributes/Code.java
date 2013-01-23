package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.code.ExceptionPool;
import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

/**
 * A code attribute, found in method structures.
 */
public class Code extends Attribute {

    private int maxStack;
    private int maxLocals;
    private byte[] codePool;
    private ExceptionPool exceptionPool;
    private AttributePool attributePool;

    /**
     * Constructs a Code attribute.
     * @param stream stream containing encoded data.
     * @param name the name, "Code".
     * @param pool the associated constant pool.
     */
    public Code(ByteStream stream, Constant name, ConstantPool pool) {
        super(name, stream.readInt());
        maxStack = stream.readShort();
        maxLocals = stream.readShort();
        codePool = stream.read(stream.readInt());
        exceptionPool = new ExceptionPool(stream);
        attributePool = new AttributePool(stream, pool);
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public Code() {

    }

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write((short) maxStack);
        out.write((short) maxLocals);
        out.write(Bytes.toByteArray(codePool.length));
        out.write(codePool);
        out.write(exceptionPool.getBytes());
        out.write(attributePool.getBytes());
        byte[] bytes = out.toByteArray();
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    /**
     * Returns the max stack.
     * @return the max stack.
     */
    public int getMaxStack() {
        return maxStack;
    }

    /**
     * Sets the max stack.
     * @param maxStack the max stack.
     */
    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    /**
     * Returns the max locals.
     * @return the max locals.
     */
    public int getMaxLocals() {
        return maxLocals;
    }

    /**
     * Sets the max locals.
     * @param maxLocals the max locals.
     */
    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    /**
     * Returns the raw code pool.
     * @return a byte[] containing all opcodes and augmenting bytes.
     */
    public byte[] getCodePool() {
        return codePool;
    }

    /**
     * Sets the raw code pool of this attribute.
     * @param codePool the pool.
     */
    public void setCodePool(byte[] codePool) {
        this.codePool = codePool;
    }

    /**
     * Fetches all Try/Catch structures in a pool.
     * @return a pool of Try/Catch structures.
     */
    public ExceptionPool getExceptionPool() {
        return exceptionPool;
    }

    /**
     * Sets the exception pool of this code segment.
     * @param exceptionPool the pool.
     */
    public void setExceptionPool(ExceptionPool exceptionPool) {
        this.exceptionPool = exceptionPool;
    }

    /**
     * Returns the attributes relating to the code.
     * @return an attribute pool.
     */
    public AttributePool getAttributePool() {
        return attributePool;
    }

    /**
     * Sets the sub-attribute pool of this attribute.
     * @param attributePool the pool.
     */
    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}
