package net.sf.jbl.introspection;

import net.sf.jbl.introspection.metadata.Metadatable;

import java.util.Collections;
import java.util.List;

/**
 * A generic class member. Can refer to either a field or a member, depending on the pool it is generated from.
 */
public class Member extends AccessibleMember implements Metadatable<Attribute>, Opcode {
    String name;
    String descriptor;
    Metadatable.Container metadata;

    public Member(int access, String name, String descriptor, Container attributes) {
        this.name = name;
        this.descriptor = descriptor;
        this.flag = access;
        this.metadata = attributes;
    }

    public Member(int access, String name, String descriptor) {
       this(access, name, descriptor, new Container(Collections.EMPTY_LIST)); //TODO: null constantpool....
    }

    /**
     * Gets the name of this member.
     *
     * @return the name of this member.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this member's name.
     *
     * @param newName the new name of the member.
     */
    public void setName(String newName) {
       name = newName;
    }

    /**
     * Gets the descriptor of this member.
     *
     * @return the descriptor of this member.
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Sets this member's type descriptor.
     *
     * @param newDescriptor the new descriptor of the member.
     */
    public void setDescriptor(String newDescriptor) {
        descriptor = newDescriptor;
    }

    /**
     * Returns the attribute pool of this member.
     *
     * @return the attribute pool of this member.
     */
    public List<Attribute> getAttributes() {
        return metadata.getAttributes();
    }

    /**
     * Sets the attribute pool of this member.
     *
     * @param attributePool the new attribute pool.
     */
    public void setAttributes(List<Attribute> attributePool) {
        metadata.setAttributes(attributePool);
    }

    /**
     * Checks if this member is marked deprecated (@Deprecated annotation or @deprecated JavaDoc tag).
     *
     * @return True if this member is deprecated, false otherwise.
     */
    public boolean isDeprecated() {
        return metadata.hasMetadata("Deprecated");
    }

    /**
     * Toggles this member's deprecated status.
     *
     * @param flag True if intent is to make this member deprecated, false if it is to make it not deprecated.
     */
    public void setDeprecated(boolean flag) {
        if (flag && !isDeprecated()) {
            metadata.addMetadata("Deprecated", null);
        } else {
            metadata.removeMetadata("Deprecated");
        }
    }

    @Override
    public void removeMetadata(String meta) {
        metadata.removeMetadata(meta);
    }

    @Override
    public Object getMetadata(String meta) {
        return metadata.getMetadata(meta);
    }

    @Override
    public boolean hasMetadata(String meta) {
        return metadata.hasMetadata(meta);
    }

    @Override
    public <V> void addMetadata(String meta, V value) {
        metadata.addMetadata(meta, value);
    }

    public boolean isStatic() {
        return is(ACC_STATIC);
    }

    public void setStatic(boolean i) {
        flag = i ? flag | ACC_STATIC : flag & ~ACC_STATIC;
    }
}
