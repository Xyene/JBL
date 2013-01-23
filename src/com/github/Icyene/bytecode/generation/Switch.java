package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.util.ByteStream;

public abstract class Switch extends Instruction {
    final int padding;
    int defaultJump, trueLen;
    int[] match, indices;

    public Switch(int address, ByteStream stream) {
        this.address = address;
        padding = (4 - (stream.position() % 4)) % 4;
        stream.read(padding);
        defaultJump = stream.readInt();
    }

    public byte[] getArguments() {
        return new ByteStream().write(new byte[padding]).write(defaultJump).toByteArray();
    }
}
