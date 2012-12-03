package com.github.Icyene.BytecodeStudio.Disassembler.Types;

public abstract class Attribute {

    private Constant name;
    private int length;

    public Attribute(int length, Constant name) {
        this.name = name;
        this.length = length;
    }

    public abstract byte[] assemble();

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
