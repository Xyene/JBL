package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;

public class Method extends Field {
    public Method(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
    }
}
