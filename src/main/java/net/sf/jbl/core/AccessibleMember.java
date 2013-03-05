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

import static net.sf.jbl.core.Opcode.*;

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

    protected boolean is(int mask) {
        return (flag & mask) > 0;
    }
}
