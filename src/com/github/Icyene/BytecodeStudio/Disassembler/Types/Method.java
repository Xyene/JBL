package com.github.Icyene.BytecodeStudio.Disassembler.Types;

import com.github.Icyene.BytecodeStudio.Disassembler.Pools.ConstantPool;

class Method extends Field {


    public Method(byte[] clazz, ConstantPool cpool, int offset) {
        super(clazz, cpool, offset);
    }
}
