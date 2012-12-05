package com.github.Icyene.bytecode.disassembler.internal;

public enum Tag {

    UTF_STRING(1),
    INTEGER(3),
    FLOAT(4),
    LONG(5),
    DOUBLE(6),
    CLASS(7),
    STRING(8),
    FIELD(9),
    METHOD(10),
    INTERFACE_METHOD(11),
    DESCRIPTOR(12),
    PHANTOM(-1); //Longs and doubles have a phantom index

    private final byte val;
    private final int len;

    Tag(int c) {
        val = (byte) c;
        len = getLengthOfByte(val);
    }

    public int getLength() {
        return len;
    }

    public byte getByte() {
        return val;
    }

    public static Tag getByValue(byte index) {
        for (Tag b : values())
            if (b.val == index)
                return b;
        return null;
    }

    int getLengthOfByte(int b) {
        switch (b) {
            case 4:
                return 5;
            case 5:
            case 6:
                return 8;
            case 7:
            case 8:
                return 2;
            case 3:
            case 9:
            case 10:
            case 11:
            case 12:
                return 4;
            default:
                return -1;
        }
    }
}