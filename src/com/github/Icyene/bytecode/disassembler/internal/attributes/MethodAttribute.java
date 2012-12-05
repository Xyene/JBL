package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;

public class MethodAttribute extends Attribute {

    public MethodAttribute(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
    }

    @Override
    public byte[] assemble() {
        return new byte[0];
    }
}
