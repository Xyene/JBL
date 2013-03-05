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

package net.sf.jbl.core.attributes;

import net.sf.jbl.core.Attribute;
import net.sf.jbl.core.AttributePool;
import net.sf.jbl.core.ConstantPool;
import net.sf.jbl.core.ByteStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A code attribute, found in method structures.
 */
public class Code extends Attribute {
    protected int maxStack;
    protected int maxLocals;
    protected byte[] codePool;
    protected List<Exception> exceptions;
    protected AttributePool metadata;

    public Code() {
        this(0, 0, new byte[0], new ArrayList<Exception>(), new AttributePool());
    }

    public Code(int maxStack, int maxLocals, byte[] code, List<Exception> exceptions, AttributePool meta) {
        super("Code");
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.codePool = code;
        this.exceptions = exceptions;
        this.metadata = meta;
    }

    public Code(ConstantPool constants, ByteStream in) {
        super("Code");
        maxStack = in.readShort();
        maxLocals = in.readShort();
        codePool = in.read(in.readInt());
        short size = in.readShort();
        exceptions = new ArrayList<Exception>(size);
        for (int i = 0; i != size; i++)
            exceptions.add(new Exception(in.readShort(), in.readShort(), in.readShort(), in.readShort()));
        metadata = AttributePool.Handler.readPool(constants, in);
    }

    public void dump(ByteStream out, ConstantPool constants) {
        ByteStream enc = ByteStream.writeStream(13);
        enc.writeShort(maxStack);
        enc.writeShort(maxLocals);
        enc.writeInt(codePool.length);
        enc.writeBytes(codePool);

        int size = exceptions.size();
        enc.writeShort(size);
        for (int i = 0; i != size; i++) {
            Exception ex = exceptions.get(i);
            enc.writeShort(ex.startPC);
            enc.writeShort(ex.endPC);
            enc.writeShort(ex.handlerPC);
            enc.writeShort(ex.catchType);
        }
        AttributePool.Handler.writePool(metadata, constants, enc);
        bytes = enc.getBuffer();
        super.dump(out, constants);
    }

    @Override
    public String toString() {
        return "{Code<S(" + maxStack + ")|L(" + maxLocals + ")|CL(" + codePool.length + ")|EX(" + exceptions + ")|A(" + metadata + ")>}";
    }

    /**
     * Returns the max stack.
     *
     * @return the max stack.
     */
    public int getMaxStack() {
        return maxStack;
    }

    /**
     * Sets the max stack.
     *
     * @param maxStack the max stack.
     */
    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    /**
     * Returns the max locals.
     *
     * @return the max locals.
     */
    public int getMaxLocals() {
        return maxLocals;
    }

    /**
     * Sets the max locals.
     *
     * @param maxLocals the max locals.
     */
    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    /**
     * Returns the raw code pool.
     *
     * @return a byte[] containing all opcodes and augmenting bytes.
     */
    public byte[] getBytecode() {
        return codePool;
    }

    /**
     * Sets the raw code pool of this attribute.
     *
     * @param bytecode the pool.
     */
    public void setBytecode(byte[] bytecode) {
        this.codePool = bytecode;
    }

    /**
     * Fetches all Try/Catch structures in a pool.
     *
     * @return a pool of Try/Catch structures.
     */
    public List<Exception> getExceptions() {
        return exceptions;
    }

    /**
     * Sets the exception pool of this code segment.
     *
     * @param exceptionPool the pool.
     */
    public void setExceptions(List<Exception> exceptionPool) {
        this.exceptions = exceptionPool;
    }

    /**
     * Returns the attributes relating to the code.
     *
     * @return an attribute pool.
     */
    public AttributePool getAttributes() {
        return metadata;
    }

    /**
     * Sets the sub-attribute pool of this attribute.
     *
     * @param attributes the pool.
     */
    public void setAttributes(AttributePool attributes) {
        metadata = attributes;
    }

    /**
     * A try/catch structure, defines blocks of code which should be protected.
     */
    public static class Exception {
        private int startPC;
        private int endPC;
        private int handlerPC;
        private int catchType;

        public Exception() {

        }

        /**
         * Constructs a Try/Catch object from the given parameters.
         *
         * @param start   startPC of block.
         * @param end     endPC of block.
         * @param handler handlerPC of block.
         * @param type    catch type of block, 0 to catch anything.
         */
        public Exception(int start, int end, int handler, int type) {
            startPC = start;
            endPC = end;
            handlerPC = handler;
            catchType = type;
        }

        /**
         * Constructs a Try/Catch object from the given parameters.
         *
         * @param start   startPC of block.
         * @param end     endPC of block.
         * @param handler handlerPC of block.
         */
        public Exception(int start, int end, int handler) {
            this(start, end, handler, 0); //catch 0 is ANY
        }

        public Exception read(ByteStream in) {
            startPC = in.readShort();
            endPC = in.readShort();
            handlerPC = in.readShort();
            catchType = in.readShort();
            return this;
        }

        /**
         * Gets a byte[] representation of this object.
         *
         * @return a byte[] representation of this object.
         */
        public void dump(ByteStream out) {
            out.writeShort((short) startPC);
            out.writeShort((short) endPC);
            out.writeShort((short) handlerPC);
            out.writeShort((short) catchType);
        }


        /**
         * Gets the startPC of this block.
         *
         * @return the startPC.
         */
        public int getStartPC() {
            return startPC;
        }

        /**
         * Sets the startPC of this block.
         *
         * @param startPC the new pc.
         */
        public void setStartPC(int startPC) {
            this.startPC = startPC;
        }

        /**
         * Gets the endPC of this block.
         *
         * @return the endPC.
         */
        public int getEndPC() {
            return endPC;
        }

        /**
         * Sets the endPC of this block.
         *
         * @param endPC the new pc.
         */
        public void setEndPC(int endPC) {
            this.endPC = endPC;
        }

        /**
         * Gets the handlerPC of this block.
         *
         * @return the handlerPC.
         */
        public int getHandlerPC() {
            return handlerPC;
        }

        /**
         * Sets the handlerPC of this block.
         *
         * @param handlerPC the new pc.
         */
        public void setHandlerPC(int handlerPC) {
            this.handlerPC = handlerPC;
        }


        /**
         * Gets the catch type of this block.
         *
         * @return the type.
         */
        public int getCatchType() {
            return catchType;
        }

        /**
         * Sets the catch type of this block.
         *
         * @param catchType the new type.
         */
        public void setCatchType(int catchType) {
            this.catchType = catchType;
        }
    }
}
