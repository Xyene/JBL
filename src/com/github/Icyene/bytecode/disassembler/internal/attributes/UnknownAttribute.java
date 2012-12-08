package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class UnknownAttribute extends Attribute {

    private byte[] value;

    public UnknownAttribute(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        value = stream.read(length);
    }

    public byte[] getBytes() {
        // System.out.println("Returning assembled attribute: " + Bytes.bytesToString(ret));
        return Bytes.concat(super.getBytes(), value);
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
