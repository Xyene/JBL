package com.github.Icyene.BytecodeStudio.Disassembler.Pools;

import com.github.Icyene.BytecodeStudio.Disassembler.Bytes;
import com.github.Icyene.BytecodeStudio.Disassembler.Indices.Constant;
import com.github.Icyene.BytecodeStudio.Disassembler.Tag;

import java.util.LinkedList;

public class ConstantPool extends LinkedList<Constant> {
    private int offset = 10;

    public ConstantPool(byte[] clazz) {
        short size = Bytes.readShort(clazz, 8);

        //Constant pool size is one greater than actual pool size
        for (int i = 1; i != size; i++) {
            byte tag = clazz[offset];
            Tag info = Tag.getByValue(tag);
            if (info == null)
                throw new UnsupportedOperationException("Error analyzing tag byte " + tag + ": not a valid tag byte");

            int len;
            byte[] section;
            //UTF Strings have to be handled differently then other values
            if (info == Tag.UTF_STRING) {
                len = Bytes.readShort(clazz, offset + 1); //info byte
                section = Bytes.slice(clazz, offset + 3, offset + 3 + len);
                add(new Constant(i, info, section));
                offset += len + 3;
                continue;
            }

            len = info.getLength();
            section = Bytes.slice(clazz, offset + 1, offset + 1 + len);
            add(new Constant(i, info, section));
            offset += len + 1;
        }
    }

    public byte[] assemble() {
        byte[] raw = Bytes.getShort((short) (size() + 1)); //Constant pool size
        for (Constant cpi : this)
            raw = Bytes.append(raw, cpi.assemble());
        return raw;
    }

    public int getSizeInBytes() {
        int totalSize = 0;
        for (Constant cpi : this)
            totalSize += cpi.getSizeInBytes();
        return totalSize;
    }

    public int getOffset() {
        return offset;
    }
}