package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;

class Method extends Field {


    public Method(byte[] clazz, ConstantPool cpool, int offset) {
        super(clazz, cpool, offset);
    }
}
