package net.sf.jbl.introspection;

import net.sf.jbl.util.ByteStream;

import static net.sf.jbl.introspection.Opcode.*;

/**
 * Base class for all accessible members.
 */
public class AccessibleMember {

    protected int flag = 0;

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public AccessibleMember() {
        super();
    }

    public AccessibleMember(int mod) {
        flag = mod;
    }

    /**
     * Returns the mask of this member.
     *
     * @return the mask of this member.
     */
    public final int getFlag() {
        return flag;
    }

    public void setFlag(int mask) {
        flag = mask;
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

    public boolean isFinal() {
        return is(ACC_FINAL);
    }

    public void setFinal(boolean i) {
        flag = i ? flag | ACC_FINAL : flag & ~ACC_FINAL;
    }

    public boolean is(int mask) {
        return (flag & mask) > 0;
    }
}
