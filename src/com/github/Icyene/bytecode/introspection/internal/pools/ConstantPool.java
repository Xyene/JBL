package com.github.Icyene.bytecode.introspection.internal.pools;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.util.LinkedList;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

/**
 * A pool to hold all Constant structures.
 */
public class ConstantPool extends LinkedList<Constant> {

    /**
     * Constructs a constant pool.
     *
     * @param stream The stream of bytes containing the pool data.
     */
    public ConstantPool(ByteStream stream) {
        short size = stream.readShort();
        byte info;
        for (int i = 1; i != size; i++) {
            switch ((int) (info = stream.readByte())) {
                case TAG_UTF_STRING:
                    add(new Constant(i, info, stream.read(stream.readShort())));
                    continue;
                case TAG_CLASS:
                case TAG_STRING:
                    add(new Constant(i, info, stream.read(2)));
                    continue;
                case TAG_INTEGER:
                case TAG_FLOAT:
                case TAG_FIELD:
                case TAG_METHOD:
                case TAG_INTERFACE_METHOD:
                case TAG_DESCRIPTOR:
                    add(new Constant(i, info, stream.read(4)));
                    continue;
                case TAG_LONG:
                case TAG_DOUBLE:
                    add(new Constant(i, info, stream.read(8)));
                    add(new Constant(++i, TAG_PHANTOM, null));
                    continue;
                default:
                    add(new Constant(i, TAG_PHANTOM, null));
            }
        }
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public ConstantPool() {
    }

    ;

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream(Bytes.toByteArray((short) (size() + 1)));
        for (Constant cpi : this) {
            out.write(cpi.getBytes());
        }
        return out.toByteArray();
    }

    /**
     * Fetches a float at the given index.
     *
     * @param in the index.
     * @return the float.
     */
    public float getFloat(int in) {
        return (float) getDouble(in);
    }

    /**
     * Fetches an integer at the given index.
     *
     * @param in the index.
     * @return the integer.
     */
    public int getInt(int in) {
        return Bytes.toInteger(get(in).getBytes(), 0);
    }

    /**
     * Fetches a long at the given index.
     *
     * @param in the index.
     * @return the long.
     */
    public long getLong(int in) {
        return Bytes.toLong(get(in).getBytes(), 0);
    }

    /**
     * Fetches a double at the given index.
     *
     * @param in the index.
     * @return the double.
     */
    public double getDouble(int in) {
        return Bytes.toDouble(get(in).getBytes(), 0);
    }

    /**
     * Fetches a String at the given index.
     *
     * @param in the index.
     * @return the String.
     */
    public String getString(int in) {
        return get(in).getStringValue();
    }

    /**
     * Fetches a Constant at the given index.
     *
     * @param in the index.
     * @return the Constant.
     */
    public Constant get(int in) {
        return super.get(in - 1);
    }

    /**
     * Overload for getting with shorts. Fetches a Constant at the given index.
     *
     * @param sh the index.
     * @return the Constant.
     */
    public Constant get(short sh) {
        return this.get((int) sh);
    }

    /**
     * Sets the constant at the given index.
     *
     * @param in the index.
     * @param c  the constant.
     * @return the given constant.
     */
    public Constant set(int in, Constant c) {
        c.setOwner(this);
        super.set(in - 1, c);
        return c;
    }

    /**
     * Constructs a Constant and adds it.
     *
     * @param type  the tag type of the Constant.
     * @param value the byte[] value of it.
     */
    public void add(int type, byte[] value) {
        add(new Constant(size() + 1, type, value));
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(Constant c) {
        if (c.getIndex() == -1) {
            c.setIndex(size());
        }
        c.setOwner(this);
        super.add(c);
        return true;
    }
}