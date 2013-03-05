/*
 *  JBL
 *  Copyright (C) 2013 Tudor Brindus
 *  All wrongs reserved.
 *
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.jbl.core;

import net.sf.jbl.core.metadata.Metadatable;

import java.util.Collections;
import java.util.List;

/**
 * A generic class member. Can refer to either a field or a member, depending on the pool it is generated from.
 */
public class Member extends AccessibleMember implements Metadatable<Attribute>, Opcode {
    String name;
    String descriptor;
    AttributePool metadata;

    public Member(int access, String name, String descriptor, AttributePool attributes) {
        this.name = name;
        this.descriptor = descriptor;
        this.flag = access;
        this.metadata = attributes;
    }

    public Member(int access, String name, String descriptor) {
       this(access, name, descriptor, new AttributePool());
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
    public AttributePool getAttributes() {
        return metadata;
    }

    /**
     * Sets the attribute pool of this member.
     *
     * @param attributePool the new attribute pool.
     */
    public void setAttributes(AttributePool attributePool) {
        metadata = attributePool;
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
