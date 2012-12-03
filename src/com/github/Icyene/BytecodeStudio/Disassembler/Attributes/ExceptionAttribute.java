package com.github.Icyene.BytecodeStudio.Disassembler.Attributes;

import com.github.Icyene.BytecodeStudio.Disassembler.Types.Attribute;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Constant;

public class ExceptionAttribute extends Attribute {
    public ExceptionAttribute(int length, Constant name) {
        super(length, name);
    }

    @Override
    public byte[] assemble() {
        return new byte[0];
    }
}
