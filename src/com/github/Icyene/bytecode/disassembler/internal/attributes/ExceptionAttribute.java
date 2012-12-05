package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;
import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;

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

    private Set<Constant> exceptionTable;

    public ExceptionAttribute(ByteStream stream, ConstantPool pool) {
        super(stream, pool);
    }

    @Override
    public byte[] assemble() {
        byte[] raw = Bytes.toByteArray((short) exceptionTable.size());
        for (Constant c : exceptionTable)
            raw = Bytes.concat(raw, Bytes.toByteArray((short) c.getIndex()));
        return Bytes.concat(super.assemble(), raw);
    }

    public Set<Constant> getExceptionTable() {
        return exceptionTable;
    }

    public void setExceptionTable(Set<Constant> exceptionTable) {
        this.exceptionTable = exceptionTable;
    }
}
