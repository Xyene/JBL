package com.github.Icyene.bytecode.disassembler.internal.pools;

import com.github.Icyene.bytecode.disassembler.internal.Tag;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.util.LinkedList;

public class ConstantPool extends LinkedList<Constant> {
    public ConstantPool(ByteStream stream) {
        short size = stream.readShort();

        for (int i = 1; i != size; i++) {
            Tag info = Tag.getByValue(stream.readByte());
            switch (info) {
                case UTF_STRING: {
                    add(new Constant(i, info, stream.read(stream.readShort())));
                    continue;
                }
                case DOUBLE:
                case LONG: {
                    add(new Constant(i, info, stream.read(info.getLength())));
                    add(new Constant(++i, Tag.PHANTOM, null));
                    continue;
                }
                default:
                    add(new Constant(i, info, stream.read(info.getLength())));
            }
        }
    }

    public byte[] assemble() {
        byte[] raw = Bytes.toByteArray((short) (size() + 1)); //Constant pool size
        for (Constant cpi : this)
            if (cpi != null) //Basically, if not phantom
                raw = Bytes.concat(raw, cpi.assemble());
        return raw;
    }

    public int getSizeInBytes() {
        int totalSize = 0;
        for (Constant cpi : this)
            totalSize += cpi.getSizeInBytes();
        return totalSize;
    }
}