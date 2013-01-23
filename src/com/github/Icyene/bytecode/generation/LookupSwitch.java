package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.util.ByteStream;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.LOOKUPSWITCH;

public class LookupSwitch extends Switch {

    public LookupSwitch(int addr, ByteStream stream) {
        super(addr, stream);
        opcode = LOOKUPSWITCH;
        address = addr;

        int length = stream.readInt();
        trueLen = (short) ((short) (9 + length << 3) + padding);
        match = indices = new int[length];

        for (int i = 0; i < length; i++) {
            match[i] = stream.readInt();
            indices[i] = stream.readInt();
        }
    }

    public byte[] getArguments() {
        ByteStream out = new ByteStream().write(super.getArguments());
        for(int i = 0; i != match.length; i++) {
            out.write(match[i]).write(indices[i]);
        }
        return out.toByteArray();
    }
}