package net.sf.jbl.introspection.members.attributes;

import net.sf.jbl.introspection.members.Attribute;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.Pool;
import net.sf.jbl.introspection.members.TryCatch;
import net.sf.jbl.util.ByteStream;
import net.sf.jbl.util.Bytes;

/**
 * A code attribute, found in method structures.
 */
public class Code extends Attribute {

    private int maxStack;
    private int maxLocals;
    private byte[] codePool;
    private Pool<TryCatch> exceptionPool;
    private Pool<Attribute> attributePool;

    /**
     * Constructs a Code attribute.
     * @param stream stream containing encoded data.
     * @param name the name, "Code".
     * @param pool the associated constant pool.
     */
    public Code(ByteStream stream, Constant name, Pool<Constant> pool) {
        super(name, stream.readInt());
        maxStack = stream.readShort();
        maxLocals = stream.readShort();
        codePool = stream.read(stream.readInt());
        exceptionPool = new Pool<TryCatch>(stream, Pool.EXCEPTION_PARSER, pool);
        attributePool = new Pool<Attribute>(stream, Pool.ATTRIBUTE_PARSER, pool);
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
        out.write(codePool.length);
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
    public Pool<TryCatch> getExceptionPool() {
        return exceptionPool;
    }

    /**
     * Sets the exception pool of this code segment.
     * @param exceptionPool the pool.
     */
    public void setExceptionPool(Pool<TryCatch> exceptionPool) {
        this.exceptionPool = exceptionPool;
    }

    /**
     * Returns the attributes relating to the code.
     * @return an attribute pool.
     */
    public Pool<Attribute> getAttributePool() {
        return attributePool;
    }

    /**
     * Sets the sub-attribute pool of this attribute.
     * @param attributePool the pool.
     */
    public void setAttributePool(Pool<Attribute> attributePool) {
        this.attributePool = attributePool;
    }
}
