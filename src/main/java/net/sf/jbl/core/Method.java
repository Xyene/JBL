/*
 *  JBL
 *  Copyright (C) 2013 Tudor Brindus
 *  All wrongs reserved.
 *
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jbl.core;

import net.sf.jbl.core.attributes.Code;

import java.util.ArrayList;
import java.util.List;

public class Method extends Member {
    Code code;

    public Method(int access, String name, String descriptor, AttributePool attributes) {
        super(access, name, descriptor, attributes);
        if ((code = (Code) getMetadata("Code")) == null)
            throw new ClassFormatError("method '" + getName() + "' has no Code attribute");
    }

    public Method(int access, String name, String descriptor) {
        super(access, name, descriptor, new AttributePool());
        code = new Code();
    }

    /**
     * Returns the max stack.
     *
     * @return the max stack.
     */
    public int getMaxStack() {
        return code.getMaxStack();
    }

    /**
     * Sets the max stack.
     *
     * @param maxStack the max stack.
     */
    public void setMaxStack(int maxStack) {
        code.setMaxStack(maxStack);
    }

    /**
     * Returns the max locals.
     *
     * @return the max locals.
     */
    public int getMaxLocals() {
        return code.getMaxLocals();
    }

    /**
     * Sets the max locals.
     *
     * @param maxLocals the max locals.
     */
    public void setMaxLocals(int maxLocals) {
        code.setMaxLocals(maxLocals);
    }

    /**
     * Returns the raw code pool.
     *
     * @return a byte[] containing all opcodes and augmenting bytes.
     */
    public byte[] getBytecode() {
        return code.getBytecode();
    }

    /**
     * Sets the raw code pool of this attribute.
     *
     * @param codePool the pool.
     */
    public void setBytecode(byte[] codePool) {
        code.setBytecode(codePool);
    }

    /**
     * Fetches all Try/Catch structures in a pool.
     *
     * @return a pool of Try/Catch structures.
     */
    public List<Code.Exception> getExceptions() {
        return code.getExceptions();
    }

    /**
     * Sets the exception pool of this code segment.
     *
     * @param exceptionPool the pool.
     */
    public void setExceptionPool(List<Code.Exception> exceptionPool) {
        code.setExceptions(exceptionPool);
    }

    /**
     * Returns the attributes relating to the code.
     *
     * @return an attribute pool.
     */
    public AttributePool getCodeAttributes() {
        return code.getAttributes();
    }

    /**
     * Sets the sub-attribute pool of this attribute.
     *
     * @param attributePool the pool.
     */
    public void setCodeAttributes(AttributePool attributePool) {
        code.setAttributes(attributePool);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
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
