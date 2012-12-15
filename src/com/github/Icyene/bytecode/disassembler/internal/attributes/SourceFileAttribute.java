package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class SourceFileAttribute extends Attribute {

    private Constant source;

    public SourceFileAttribute(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        source = pool.get(stream.readShort() );
    }

    public byte[] getBytes() {
        return Bytes.concat(super.getBytes(), Bytes.toByteArray((short)source.getIndex()));
    }

    public String getSource() {
        return source.getStringValue();
    }

    public void setSource(String source) {
        this.source.setValue(source.getBytes());
    }
}
