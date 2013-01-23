package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.util.Bytes;

public class Instruction {
    int address, opcode;
    byte[] args;
    boolean wide = false;

    public Instruction(int opcode, boolean wide, int address, byte[] args) {
        this.opcode = opcode;
        this.wide = wide;
        this.address = address;
        this.args = args;
    }

    public Instruction(int opcode, int address, byte[] args) {
        this(opcode, false, address, args);
    }

    public Instruction() {
    }

    public byte[] getBytes() {
        return Bytes.prepend(getArguments(), (byte) opcode);
    }

    public byte[] getArguments() {
        return args;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public int getAddress() {
        return address;
    }

    public String toString() {
        return String.format("[Op @ %s of type %s with args %s]", address, opcode, Bytes.bytesToString(args));
    }
}