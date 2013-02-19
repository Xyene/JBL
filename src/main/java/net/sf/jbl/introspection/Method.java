package net.sf.jbl.introspection;

import net.sf.jbl.introspection.attributes.Code;

import java.util.ArrayList;
import java.util.List;

public class Method extends Member {
    Code code;

    public Method( int access, String name, String descriptor, Container attributes) {
        super(access, name, descriptor, attributes);
        if((code = (Code)getMetadata("Code")) == null) throw new ClassFormatError("method '" + getName() + "' has no Code attribute");
    }

    public Method(int access, String name, String descriptor) {
        super(access, name, descriptor, new Container(new ArrayList<Attribute>(1))); //TODO: null constantpool....
        metadata.getAttributes().add(new Code());
    }

    /**
     * Returns the max stack.
     * @return the max stack.
     */
    public int getMaxStack() {
        return code.getMaxStack();
    }

    /**
     * Sets the max stack.
     * @param maxStack the max stack.
     */
    public void setMaxStack(int maxStack) {
        code.setMaxStack(maxStack);
    }

    /**
     * Returns the max locals.
     * @return the max locals.
     */
    public int getMaxLocals() {
        return code.getMaxLocals();
    }

    /**
     * Sets the max locals.
     * @param maxLocals the max locals.
     */
    public void setMaxLocals(int maxLocals) {
        code.setMaxLocals(maxLocals);
    }

    /**
     * Returns the raw code pool.
     * @return a byte[] containing all opcodes and augmenting bytes.
     */
    public byte[] getBytecode() {
        return code.getBytecode();
    }

    /**
     * Sets the raw code pool of this attribute.
     * @param codePool the pool.
     */
    public void setBytecode(byte[] codePool) {
        code.setBytecode(codePool);
    }

    /**
     * Fetches all Try/Catch structures in a pool.
     * @return a pool of Try/Catch structures.
     */
    public List<Code.Exception> getExceptions() {
        return code.getExceptions();
    }

    /**
     * Sets the exception pool of this code segment.
     * @param exceptionPool the pool.
     */
    public void setExceptionPool(List<Code.Exception> exceptionPool) {
        code.setExceptions(exceptionPool);
    }

    /**
     * Returns the attributes relating to the code.
     * @return an attribute pool.
     */
    public List<Attribute> getCodeAttributes() {
        return code.getAttributes();
    }

    /**
     * Sets the sub-attribute pool of this attribute.
     * @param attributePool the pool.
     */
    public void setCodeAttributes(List<Attribute> attributePool) {
        code.setAttributes(attributePool);
    }

    public boolean isBridge() {
        return is(ACC_BRIDGE);
    }

    public boolean setBridge() {
        return is(ACC_BRIDGE);
    }

    public boolean isVarargs() {
        return is(ACC_VARARGS);
    }

    public boolean setVarargs() {
        return is(ACC_VARARGS);
    }

    public boolean isSynchronized() {
        return is(ACC_SYNCHRONIZED);
    }

    public void setSynchronized(boolean i) {
        flag = i ? flag | ACC_SYNCHRONIZED : flag & ~ACC_SYNCHRONIZED;
    }

    public boolean isNative() {
        return is(ACC_NATIVE);
    }

    public void setNative(boolean i) {
        flag = i ? flag | ACC_NATIVE : flag & ~ACC_NATIVE;
    }

    public boolean isAbstract() {
        return is(ACC_ABSTRACT);
    }

    public void setAbstract(boolean i) {
        flag = i ? flag | ACC_ABSTRACT : flag & ~ACC_ABSTRACT;
    }

    public boolean isStrict() {
        return is(ACC_STRICT);
    }

    public void setStrict(boolean i) {
        flag = i ? flag | ACC_STRICT : flag & ~ACC_STRICT;
    }
}
