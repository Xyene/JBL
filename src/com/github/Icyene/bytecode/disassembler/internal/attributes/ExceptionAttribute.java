package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.HashSet;
import java.util.Set;

public class ExceptionAttribute extends Attribute {

    /*
     Exceptions_attribute {
    	u2 attribute_name_index;
    	u4 attribute_length;
    	u2 number_of_exceptions;
    	u2 exception_index_table[number_of_exceptions];
    }
     */

    private Set<Constant> exceptionTable = new HashSet<Constant>();

    public ExceptionAttribute(ByteStream stream,  Constant name, ConstantPool pool) {
        super(stream, name, pool);
        short size = stream.readShort();
        if (size <= 0) return;
        for (int i = 0; i != size; i++) {
            exceptionTable.add(pool.get(i ));
        }
    }

    @Override
    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) exceptionTable.size());
        for (Constant c : exceptionTable)
            raw = Bytes.concat(raw, Bytes.toByteArray((short) c.getIndex()));
        return Bytes.concat(super.getBytes(), raw);
    }

    public Set<Constant> getExceptionTable() {
        return exceptionTable;
    }

    public void setExceptionTable(Set<Constant> exceptionTable) {
        this.exceptionTable = exceptionTable;
    }
}
