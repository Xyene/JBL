package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.internal.metadata.Opcode;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class TableSwitch extends Switch {
    int length;

    public TableSwitch(int addr, ByteStream stream) {
        super(addr, stream);
        address = addr;
        opcode = Opcode.TABLESWITCH;

        int low = stream.readInt(), high = stream.readInt();

        length = high - low + 1;
        trueLen = (short) ((13 + length * 4) + padding);

        match = new int[length];
        indices = new int[length];

        for (int i = low; i <= high; i++)
            match[i - low] = i;

        for (int i = 0; i < length; i++) {
            indices[i] = stream.readInt();
        }

        System.out.println("Parsed table switch: " + match[0] + ", " +  indices[0] + " | " + match[1] + ", " +  indices[1]+ " | " + match[2] + ", " +  indices[2]);
    }

    public byte[] getArguments() {
        ByteStream out = new ByteStream(super.getArguments()).write((length > 0)? match[0] : 0).write((length > 0)? match[length - 1] : 0);
        for(int i=0; i < length; i++)
            out.write(indices[i]);
        return out.toByteArray();
    }
}
