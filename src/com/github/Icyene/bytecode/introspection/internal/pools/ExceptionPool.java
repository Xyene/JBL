package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.ArrayList;

public class ExceptionPool extends ArrayList<TryCatch> {

    public ExceptionPool(ByteStream stream) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new TryCatch(stream));
    }

    public ExceptionPool() {

    }

    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) size());
        for (TryCatch e : this)
            raw = Bytes.concat(raw, e.getBytes());
        return raw;
    }
}
