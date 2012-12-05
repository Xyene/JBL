package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class IgnoredAttribute extends Attribute {

    private byte[] value;

    public IgnoredAttribute(ByteStream stream) {
        this(stream, null);
    }

    public IgnoredAttribute(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
        value = stream.read(length);
    }

    public byte[] assemble() {
        // System.out.println("Returning assembled attribute: " + Bytes.bytesToString(ret));
        return Bytes.concat(super.assemble(), value);
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
