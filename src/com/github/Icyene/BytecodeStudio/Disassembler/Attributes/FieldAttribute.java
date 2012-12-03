package com.github.Icyene.BytecodeStudio.Disassembler.Attributes;

import com.github.Icyene.BytecodeStudio.Disassembler.Types.Attribute;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Constant;

public class FieldAttribute extends Attribute {
    public FieldAttribute(int length, Constant name) {
        super(length, name);
    }

    @Override
    public byte[] assemble() {
        return new byte[0];
    }
}
