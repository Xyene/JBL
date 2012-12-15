package com.github.Icyene.bytecode.disassembler.internal.attributes;

import com.github.Icyene.bytecode.disassembler.internal.objects.Attribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.AttributePool;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.internal.pools.ExceptionPool;
import com.github.Icyene.bytecode.disassembler.internal.pools.InstructionPool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

public class CodeAttribute extends Attribute {

    /*
    Code_attribute {
    	u2 attribute_name_index;
    	u4 attribute_length;
    	u2 max_stack;
    	u2 max_locals;
    	u4 code_length;
    	u1 code[code_length];
    	u2 exception_table_length;
    	{    	u2 start_pc;
    	      	u2 end_pc;
    	      	u2  handler_pc;
    	      	u2  catch_type;
    	}	exception_table[exception_table_length];
    	u2 attributes_count;
    	attribute_info attributes[attributes_count];
    }
     */

    private short maxStack;
    private short maxLocals;
    private InstructionPool codePool;
    private ExceptionPool exceptionPool;
    private AttributePool attributePool;

    public CodeAttribute(ByteStream stream, Constant name, ConstantPool pool) {
        super(stream, name, pool);
        maxStack = stream.readShort();
        maxLocals = stream.readShort();
        codePool = new InstructionPool(stream);
        System.out.println("Code pool: " + codePool);
        exceptionPool = new ExceptionPool(stream);
        attributePool = new AttributePool(stream, pool);

    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(super.getBytes());
        out.write(Bytes.toByteArray(maxStack));
        out.write(Bytes.toByteArray(maxLocals));
        out.write(codePool.getBytes());
        out.write(exceptionPool.getBytes());
        out.write(attributePool.getBytes());
        return out.toByteArray();
    }

    public short getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(short maxStack) {
        this.maxStack = maxStack;
    }

    public short getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(short maxLocals) {
        this.maxLocals = maxLocals;
    }

    public InstructionPool getCodePool() {
        return codePool;
    }

    public void setCodePool(InstructionPool codePool) {
        this.codePool = codePool;
    }

    public ExceptionPool getExceptionPool() {
        return exceptionPool;
    }

    public void setExceptionPool(ExceptionPool exceptionPool) {
        this.exceptionPool = exceptionPool;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}
