package com.github.Icyene.bytecode.introspection.internal.members;

import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;
import java.lang.Exception;
import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;

public class Constant {
    private int index;
    private int type;
    private byte[] value;
    private ConstantPool owner;

    public Constant(int index, int type, byte[] value, ConstantPool owner) {
        this.index = index;
        this.type = type;
        this.value = value;
        this.owner = owner;
    }

    public Constant(ByteStream stream, ConstantPool owner) {

    }


    public byte[] getBytes() {
        if (type == TAG_UTF_STRING)
            return Bytes.prepend(Bytes.concat(Bytes.toByteArray((short) value.length), value), (byte) type);
        if (type == TAG_PHANTOM)
            return new byte[]{};
        return Bytes.prepend(value, (byte) type);
    }



    public byte[] getRawValue() {
        return value;
    }

    public void setRawValue(byte[] value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public int getType() {
        return type;
    }

   /* @Override
    public String toString() {
        return "[I=" + index + ", T=" + type.name() + ", V='" + getStringValue() + "']";
    }  */

    @Deprecated
    public String getStringValue() {
        return toString();
    }


    public String toString() {
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
