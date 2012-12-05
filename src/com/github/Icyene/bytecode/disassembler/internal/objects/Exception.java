package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.util.ByteStream;

public class Exception {

    private short startPC;
    private short endPC;
    private short handlerPC;
    private short catchType;

    public Exception(ByteStream stream) {
        startPC = stream.readShort();
        endPC = stream.readShort();
        handlerPC = stream.readShort();
        catchType = stream.readShort();
    }

    public short getStartPC() {
        return startPC;
    }

    public void setStartPC(short startPC) {
        this.startPC = startPC;
    }

    public short getEndPC() {
        return endPC;
    }

    public void setEndPC(short endPC) {
        this.endPC = endPC;
    }

    public short getHandlerPC() {
        return handlerPC;
    }

    public void setHandlerPC(short handlerPC) {
        this.handlerPC = handlerPC;
    }

    public short getCatchType() {
        return catchType;
    }

    public void setCatchType(short catchType) {
        this.catchType = catchType;
    }
}
