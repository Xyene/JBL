package com.github.Icyene.BytecodeStudio.Disassembler.Indices;

import com.github.Icyene.BytecodeStudio.Disassembler.Bytes;
import com.github.Icyene.BytecodeStudio.Disassembler.Tag;

public class Constant {
    int index;
    Tag type;
    byte[] value;

    public Constant(int index, Tag type, byte[] value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    public byte[] assemble() {
        if (type == Tag.UTF_STRING)
            return Bytes.prepend(Bytes.append(Bytes.getShort((short) value.length), value), (byte) type.getByte());
        return Bytes.prepend(value, (byte) type.getByte());
    }

    public void setType(Tag type) {
        this.type = type;
    }

    public Tag getType() {
        return type;
    }

    public int getIndex() {
        return index - 1;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public int getSizeInBytes() {
        if (type == Tag.UTF_STRING)
            return value.length + 1;
        return value.length;
    }

    @Override
    public String toString() {
        return "[I=" + index + ", T=" + type.name() + ", V='" + prettyPrint() + "']";
    }

    public String prettyPrint() {
        try {
            switch (type) {
                case UTF_STRING:
                    return new String(value, "UTF-8");
                case STRING:
                case CLASS:
                    return  "#" + Bytes.readShort(value, 0);
                case METHOD:
                case FIELD:
                case INTERFACE_METHOD:
                    return "#" + Bytes.readShort(value, 0) + ".#" + Bytes.readShort(value, 2);
                case DESCRIPTOR:
                    return "#" + Bytes.readShort(value, 0) + ":#" + Bytes.readShort(value, 2);
                default:
                    return value.toString();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not get String value of " + value + "(type=" + type.name() + ")");
        }
    }
}
