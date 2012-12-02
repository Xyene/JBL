package com.github.Icyene.BytecodeStudio.Disassembler.Types;

import com.github.Icyene.BytecodeStudio.Disassembler.Bytes;

public class Attribute {

    private Constant name;
    private byte[] info;

    public Attribute(byte[] info, Constant name) {
        this.name = name;
        this.info = info;
    }

    public byte[] assemble() {
        return Bytes.append(Bytes.append(Bytes.getShort((short) name.getIndex()), Bytes.getInt(info.length)), info);
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }
}
