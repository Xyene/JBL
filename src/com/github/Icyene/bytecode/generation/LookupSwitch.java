package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.util.ByteStream;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.LOOKUPSWITCH;

public class LookupSwitch extends Switch {

    public LookupSwitch(int addr, ByteStream stream) {
        super(addr, stream);
        opcode = LOOKUPSWITCH;

        length = stream.readInt();
        trueLen = (short) ((short) (12 + (length << 3)) + padding);

        match = new int[length];
        indices = new int[length];

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