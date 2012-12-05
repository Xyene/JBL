package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;

public class FieldAttribute extends Attribute {
    public FieldAttribute(int length, Constant name) {
        super(length, name);
    }

    @Override
    public byte[] assemble() {
        return new byte[0];
    }
}
