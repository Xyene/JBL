package com.github.Icyene.bytecode.introspection.internal.code;

import com.github.Icyene.bytecode.introspection.internal.members.TryCatch;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.ArrayList;

public class ExceptionPool extends ArrayList<TryCatch> {

    /**
     * Constructs an exception pool.
     *
     * @param stream The stream of bytes containing the pool data.
     */
    public ExceptionPool(ByteStream stream) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new TryCatch(stream));
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public ExceptionPool() {
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) size());
        for (TryCatch e : this)
            raw = Bytes.concat(raw, e.getBytes());
        return raw;
    }
}
