package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

public class TryCatch {

    private int startPC;
    private int endPC;
    private int handlerPC;
    private int catchType;

    public TryCatch(ByteStream stream) {
        this(stream.readShort(), stream.readShort(), stream.readShort(), stream.readShort());
    }

    public TryCatch() {

    }

    public TryCatch(int start, int end, int handler, int type) {
        startPC = start;
        endPC = end;
        handlerPC = handler;
        catchType = type;
    }

    public TryCatch(int start, int end, int handler) {
        this(start, end, handler, 0); //catch 0 is ANY
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream(Bytes.toByteArray((short) startPC));
        out.write((short) endPC);
        out.write((short) handlerPC);
        out.write((short) catchType);
        return out.toByteArray();
    }

    public int getStartPC() {
        return startPC;
    }

    public void setStartPC(int startPC) {
        this.startPC = startPC;
    }

    public int getEndPC() {
        return endPC;
    }

    public void setEndPC(int endPC) {
        this.endPC = endPC;
    }

    public int getHandlerPC() {
        return handlerPC;
    }

    public void setHandlerPC(int handlerPC) {
        this.handlerPC = handlerPC;
    }

    public int getCatchType() {
        return catchType;
    }

    public void setCatchType(int catchType) {
        this.catchType = catchType;
    }
}
