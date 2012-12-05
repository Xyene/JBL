package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class ConstantValueAttribute extends Attribute {

    private Constant value;

    public ConstantValueAttribute(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
        this.value = pool.get(stream.readShort() - 1);
    }

    @Override
    public byte[] assemble() {
        return Bytes.concat(super.assemble(), Bytes.toByteArray((short) value.getIndex()));
    }

    public Constant getValue() {
        return value;
    }

    public void setValue(Constant value) {
        this.value = value;
    }
}