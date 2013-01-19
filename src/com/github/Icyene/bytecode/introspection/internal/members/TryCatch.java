package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public class TryCatch {

    private int startPC;
    private int endPC;
    private int handlerPC;
    private int catchType;

    /**
     * Constructs a Try/Catch object from the given stream.
     *
     * @param stream The stream the block is encoded in.
     */
    public TryCatch(ByteStream stream) {
        this(stream.readShort(), stream.readShort(), stream.readShort(), stream.readShort());
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public TryCatch() {

    }

    /**
     * Constructs a Try/Catch object from the given parameters.
     *
     * @param start   startPC of block.
     * @param end     endPC of block.
     * @param handler handlerPC of block.
     * @param type    catch type of block, 0 to catch anything.
     */
    public TryCatch(int start, int end, int handler, int type) {
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
    public TryCatch(int start, int end, int handler) {
        this(start, end, handler, 0); //catch 0 is ANY
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream(Bytes.toByteArray((short) startPC));
        out.write((short) endPC);
        out.write((short) handlerPC);
        out.write((short) catchType);
        return out.toByteArray();
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
