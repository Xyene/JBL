package com.github.Icyene.BytecodeStudio.Disassembler.Types;

import com.github.Icyene.BytecodeStudio.Disassembler.Bytes;

public class Flag {

    //As close to an enum as possible :-)
    public static final int
            ACC_PUBLIC = 0x0001,
            ACC_PRIVATE = 0x0002,
            ACC_PROTECTED = 0x0004,
            ACC_STATIC = 0x0008,
            ACC_FINAL = 0x0010,
            ACC_SYNCHRONIZED = 0x0020,
            ACC_NATIVE = 0x0100,
            ACC_ABSTRACT = 0x0400,
            ACC_STRICT = 0x0800;

    private final int flag;

    public Flag(int flag) {
        this.flag = flag;
    }

    public byte[] assemble() {
        return Bytes.getShort((short) flag);
    }

    public boolean isPublic() {
        return is(ACC_PUBLIC);
    }

    public boolean isPrivate() {
        return is(ACC_PRIVATE);
    }

    public boolean isProtected() {
        return is(ACC_PROTECTED);
    }

    public boolean isStatic() {
        return is(ACC_STATIC);
    }

    public boolean isFinal() {
        return is(ACC_FINAL);
    }

    public boolean isSynchronized() {
        return is(ACC_SYNCHRONIZED);
    }

    public boolean isNative() {
        return is(ACC_NATIVE);
    }

    public boolean isAbstract() {
        return is(ACC_ABSTRACT);
    }

    public boolean isStrict() {
        return is(ACC_STRICT);
    }

    boolean is(int mask) {
        return (flag & mask) == 1;
    }
}
