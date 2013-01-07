package com.github.Icyene.bytecode.introspection.internal;

import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.*;


public class AccessibleMember {

    protected int flag = 0;

    public AccessibleMember() {
        super();
    }

    public final strictfp int getMask() {
        return flag;
    }

    public byte[] getBytes() {
        return Bytes.toByteArray((short) flag);
    }

    public boolean isPublic() {
        return is(ACC_PUBLIC);
    }

    public void setPublic(boolean i) {
        flag = i ? flag | ACC_PUBLIC : flag & ~ACC_PUBLIC;
    }

    public boolean isPrivate() {
        return is(ACC_PRIVATE);
    }

    public void setPrivate(boolean i) {
        flag = i ? flag | ACC_PRIVATE : flag & ~ACC_PRIVATE;
    }

    public boolean isProtected() {
        return is(ACC_PROTECTED);
    }

    public void setProtected(boolean i) {
        flag = i ? flag | ACC_PROTECTED : flag & ~ACC_PROTECTED;
    }

    public boolean isStatic() {
        return is(ACC_STATIC);
    }

    public void setStatic(boolean i) {
        flag = i ? flag | ACC_STATIC : flag & ~ACC_STATIC;
    }

    public boolean isFinal() {
        return is(ACC_FINAL);
    }

    public void setFinal(boolean i) {
        flag = i ? flag | ACC_FINAL : flag & ~ACC_FINAL;
    }

    public boolean isAbstract() {
        return is(ACC_ABSTRACT);
    }

    public void setAbstract(boolean i) {
        flag = i ? flag | ACC_ABSTRACT : flag & ~ACC_ABSTRACT;
    }

    public boolean isStrict() {
        return is(ACC_STRICT);
    }

    public void setStrict(boolean i) {
        flag = i ? flag | ACC_STRICT : flag & ~ACC_STRICT;
    }

    public boolean is(int mask) {
        return (flag & mask) > 0;
    }
}
