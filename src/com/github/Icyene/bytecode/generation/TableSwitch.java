package com.github.Icyene.bytecode.generation;

import com.github.Icyene.bytecode.introspection.internal.metadata.Opcode;
import com.github.Icyene.bytecode.introspection.util.ByteStream;

public class TableSwitch extends Switch {

    public TableSwitch(int addr, ByteStream stream) {
        super(addr, stream);
        opcode = Opcode.TABLESWITCH;

        int low = stream.readInt(), high = stream.readInt();

        length = high - low + 1;
        trueLen = (short) ((12 + (length << 2)) + padding);
        match = new int[length];
        indices = new int[length];

        for (int i = low; i <= high; i++)
            match[i - low] = i;
        for (int i = 0; i < length; i++)
            indices[i] = stream.readInt();
    }

    public byte[] getArguments() {
        ByteStream out = new ByteStream(super.getArguments()).write((length > 0) ? match[0] : 0).write((length > 0) ? match[length - 1] : 0);
        for (int i = 0; i < length; i++)
            out.write(indices[i]);
        return out.toByteArray();
    }
}
