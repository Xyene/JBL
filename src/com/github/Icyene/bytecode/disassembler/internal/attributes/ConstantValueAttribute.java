package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class ConstantValueAttribute extends Attribute {

    private Constant value;

    public ConstantValueAttribute(ByteStream stream,  Constant name,ConstantPool pool) {
        super(stream, name, pool);
        this.value = pool.get(stream.readShort() );
    }

    @Override
    public byte[] getBytes() {
        return Bytes.concat(super.getBytes(), Bytes.toByteArray((short) value.getIndex()));
    }

    public Constant getValue() {
        return value;
    }

    public void setValue(Constant value) {
        this.value = value;
    }
}