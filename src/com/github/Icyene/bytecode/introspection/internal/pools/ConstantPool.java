package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.metadata.Tag;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

public class ConstantPool extends LinkedList<Constant> {
    public ConstantPool(ByteStream stream) {
        short size = stream.readShort();

        for (int i = 1; i != size; i++) {
            Tag info = Tag.getByValue(stream.readByte());
            switch (info) {
                case UTF_STRING: {
                    add(new Constant(i, info, stream.read(stream.readShort()), this));
                    continue;
                }
                case DOUBLE:
                case LONG: {
                    add(new Constant(i, info, stream.read(info.getLength()), this));
                    add(new Constant(++i, Tag.PHANTOM, null, this));
                    continue;
                }
                default:
                    add(new Constant(i, info, stream.read(info.getLength()), this));
            }
        }
    }

    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) (size() + 1)); //Constant pool size
        for (Constant cpi : this)
            raw = Bytes.concat(raw, cpi.getBytes());
        return raw;
    }

    public Constant get(int in) {
        return super.get(in - 1);
    }

    public Constant get(short sh) {
        return this.get((int) sh);
    }

    public void add(Tag type, byte[] value) {
        add(new Constant(size() + 1, type, value, this));
    }
}