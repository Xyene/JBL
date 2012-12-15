package com.github.Icyene.bytecode.disassembler.internal.objects;

import com.github.Icyene.bytecode.disassembler.internal.Tag;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.lang.Exception;

public class Constant {
    private final int index;
    private Tag type;
    private byte[] value;

    public Constant(int index, Tag type, byte[] value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    public byte[] getBytes() {
        if (type == Tag.UTF_STRING)
            return Bytes.prepend(Bytes.concat(Bytes.toByteArray((short) value.length), value), (byte) Tag.UTF_STRING.getByte());
        if (type == Tag.PHANTOM)
            return new byte[]{};
        return Bytes.prepend(value, (byte) type.getByte());
    }

    public void setType(Tag type) {
        this.type = type;
    }

    public Tag getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[I=" + index + ", T=" + type.name() + ", V='" + getStringValue() + "']";
    }

    public String getStringValue() {
        try {
            switch (type) {
                case UTF_STRING:
                    return new String(value, "UTF-8");
                case STRING:
                case CLASS:
                    return "#" + Bytes.toShort(value, 0);
                case METHOD:
                case FIELD:
                case INTERFACE_METHOD:
                    return "#" + Bytes.toShort(value, 0) + ".#" + Bytes.toShort(value, 2);
                case DESCRIPTOR:
                    return "#" + Bytes.toShort(value, 0) + ":#" + Bytes.toShort(value, 2);
                case PHANTOM:
                    return "PHANTOM_INDEX";
                case DOUBLE:
                    return Bytes.toDouble(value, 0) + "";
                default:
                    return value.toString();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not get String value of " + value + "(type=" + type.name() + ")");
        }
    }
}
