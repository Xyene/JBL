package tk.jblib.bytecode.introspection;

import tk.jblib.bytecode.util.Bytes;

import static tk.jblib.bytecode.introspection.metadata.Opcode.*;

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

    /**
     * Returns the mask of this member.
     *
     * @return the mask of this member.
     */
    public final int getMask() {
        return flag;
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        return Bytes.toByteArray((short) flag);
    }

    /**
     * Is this member public?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isPublic() {
        return is(ACC_PUBLIC);
    }

    /**
     * Toggles the public flag of this member.
     *
     * @param i True if intent is to mark member public, false otherwise.
     */
    public void setPublic(boolean i) {
        flag = i ? flag | ACC_PUBLIC : flag & ~ACC_PUBLIC;
    }

    /**
     * Is this member private?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isPrivate() {
        return is(ACC_PRIVATE);
    }

    /**
     * Toggles the private flag of this member.
     *
     * @param i True if intent is to mark member private, false otherwise.
     */
    public void setPrivate(boolean i) {
        flag = i ? flag | ACC_PRIVATE : flag & ~ACC_PRIVATE;
    }

    /**
     * Is this member protected?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isProtected() {
        return is(ACC_PROTECTED);
    }

    /**
     * Toggles the protected flag of this member.
     *
     * @param i True if intent is to mark member protected, false otherwise.
     */
    public void setProtected(boolean i) {
        flag = i ? flag | ACC_PROTECTED : flag & ~ACC_PROTECTED;
    }

    /**
     * Is this member static?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isStatic() {
        return is(ACC_STATIC);
    }

    /**
     * Toggles the static flag of this member.
     *
     * @param i True if intent is to mark member static, false otherwise.
     */
    public void setStatic(boolean i) {
        flag = i ? flag | ACC_STATIC : flag & ~ACC_STATIC;
    }

    /**
     * Is this member final?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isFinal() {
        return is(ACC_FINAL);
    }

    /**
     * Toggles the final flag of this member.
     *
     * @param i True if intent is to mark member final, false otherwise.
     */
    public void setFinal(boolean i) {
        flag = i ? flag | ACC_FINAL : flag & ~ACC_FINAL;
    }

    /**
     * Is this member abstract?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isAbstract() {
        return is(ACC_ABSTRACT);
    }

    /**
     * Toggles the abstract flag of this member.
     *
     * @param i True if intent is to mark member abstract, false otherwise.
     */
    public void setAbstract(boolean i) {
        flag = i ? flag | ACC_ABSTRACT : flag & ~ACC_ABSTRACT;
    }

    /**
     * Is this member strictfp?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isStrict() {
        return is(ACC_STRICT);
    }

    /**
     * Toggles the strictfp flag of this member.
     *
     * @param i True if intent is to mark member native, false otherwise.
     */
    public void setStrict(boolean i) {
        flag = i ? flag | ACC_STRICT : flag & ~ACC_STRICT;
    }

    /**
     * Checks if this member matches the given mask, determined by a flag & mask > 0 operation.
     *
     * @return True if it does, false otherwise.
     */
    public boolean is(int mask) {
        return (flag & mask) > 0;
    }
}
