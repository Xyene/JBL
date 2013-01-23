package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

/**
 * A constant, found in constant pools and referenced by practically everything.
 */
public class Constant {
    private int index;
    private int type;
    private byte[] value;
    private ConstantPool owner;

    /**
     * Creates a constant with the given parameters.
     *
     * @param index the index of the constant.
     * @param type  the tag type of the constant.
     * @param value the value of the constant in byte[] format.
     */
    public Constant(int index, int type, byte[] value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    /**
     * Creates a constant with the given parameters.
     *
     * @param type  the tag type of the constant.
     * @param value the value of the constant in byte[] format.
     */
    public Constant(int type, byte[] value) {
        this(-1, type, value);
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public Constant() {
    }

    public byte[] getBytes() {
        if (type == TAG_UTF_STRING)
            return Bytes.prepend(Bytes.concat(Bytes.toByteArray((short) value.length), value), (byte) type);
        if (type == TAG_PHANTOM)
            return new byte[]{};
        return Bytes.prepend(value, (byte) type);
    }

    /**
     * Returns the byte[] value of this constant. Should not be confused with {@link #getBytes()}.
     *
     * @return a byte[] value.
     */
    public byte[] getRawValue() {
        return value;
    }

    /**
     * Sets the value of this constant.
     *
     * @param value the new value.
     */
    public void setRawValue(byte[] value) {
        this.value = value;
    }

    /**
     * Fetches the owning constant pool.
     *
     * @return the owning constant pool.
     */
    public ConstantPool getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the pool. Should not be accessed directly: ConstantPool.add sets it.
     *
     * @param owner the owning pool.
     */
    public void setOwner(ConstantPool owner) {
        this.owner = owner;
    }

    /**
     * Fetches the index of this constant.
     *
     * @return the new index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of this constant.
     *
     * @param index the new index.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the tag byte of this constant as an int.
     *
     * @return the tag.
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the tag of this constant.
     *
     * @param type the new tag.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[I=" + index + ", T=" + Integer.toHexString(type) + ", V='" + getStringValue() + "']";
    }

    /**
     * Pretty-prints this constant's value, redirecting wrappers to raw values.
     *
     * @return the true value of this constant.
     */
    public String getStringValue() {
        try {
            switch (type) {
                case TAG_UTF_STRING:
                    return new String(value, "UTF-8");
                case TAG_STRING:
                case TAG_CLASS:
                    System.out.println("Redirecting to " + owner.get(Bytes.toShort(value, 0)).toString());
                    return owner.get(Bytes.toShort(value, 0)).toString();
                case TAG_METHOD:
                case TAG_FIELD:
                case TAG_INTERFACE_METHOD:
                    return "#" + Bytes.toShort(value, 0) + ".#" + Bytes.toShort(value, 2);
                case TAG_DESCRIPTOR:
                    return "#" + Bytes.toShort(value, 0) + ":#" + Bytes.toShort(value, 2);
                case TAG_PHANTOM:
                    return "PHANTOM_INDEX";
                case TAG_DOUBLE:
                    return Bytes.toDouble(value, 0) + "D";
                case TAG_LONG:
                    return Bytes.toLong(value, 0) + "L";
                case TAG_INTEGER:
                    return Bytes.toInteger(value, 0) + "";
                default:
                    return value.toString();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not get String value of " + Integer.toHexString(type));
        }
    }
}
