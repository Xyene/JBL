package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.members.constants.Descriptor;
import com.github.Icyene.bytecode.introspection.internal.members.constants.MemberRef;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class ConstantPool extends LinkedList<Constant> {
    public ConstantPool(ByteStream stream) {
        short size = stream.readShort();
        byte info;
        for (int i = 1; i != size; i++) {
            switch ((int) (info = stream.readByte())) {
                case TAG_UTF_STRING:
                    add(new Constant(i, info, stream.read(stream.readShort()), this));
                    continue;
                case TAG_CLASS:
                case TAG_STRING:
                    add(new Constant(i, info, stream.read(2), this));
                    continue;
                case TAG_INTEGER:
                case TAG_FLOAT:
                case TAG_FIELD:
                case TAG_METHOD:
                case TAG_INTERFACE_METHOD:
                case TAG_DESCRIPTOR:
                    add(new Constant(i, info, stream.read(4), this));
                    continue;
                case TAG_LONG:
                case TAG_DOUBLE:
                    add(new Constant(i, info, stream.read(8), this));
                    add(new Constant(++i, TAG_PHANTOM, null, this));
                    continue;
                default:
                    add(new Constant(i, TAG_PHANTOM, null, this));
            }
        }
    }

    public Descriptor getDescriptor(int in) {
        return new Descriptor(new ByteStream(get(in).getBytes()), this);
    }

    public float getFloat(int in) {
        return (float) getDouble(in);
    }

    public int getInt(int in) {
        return Bytes.toInteger(get(in).getBytes(), 0);
    }

    public long getLong(int in) {
        return Bytes.toLong(get(in).getBytes(), 0);
    }

    public double getDouble(int in) {
        return Bytes.toDouble(get(in).getBytes(), 0);
    }

    public MemberRef getMember(int in) {
        return new MemberRef(new ByteStream(get(in).getBytes()), this);
    }

    public String getString(int in) {
        Constant str = get(in);
        return str.getType() == TAG_STRING ? getString(Bytes.toShort(str.getBytes(), 0)) : str.getStringValue();
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream(Bytes.toByteArray((short) (size() + 1)));
        for (Constant cpi : this) {
            out.write(cpi.getBytes());
        }
        return out.toByteArray();
    }

    public Constant get(int in) {
        return super.get(in - 1);
    }

    public Constant get(short sh) {
        return this.get((int) sh);
    }

    public Constant set(int in, Constant con) {
        super.set(in - 1, con);
        return con;
    }

    public void add(int type, byte[] value) {
        add(new Constant(size() + 1, type, value, this));
    }
}