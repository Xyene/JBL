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

import java.util.Collections;

public class Field extends Member {

    public Field(int access, String name, String descriptor, AttributePool attributes) {
        super(access, name, descriptor, attributes);
    }

    public Field(int access, String name, String descriptor) {
        this(access, name, descriptor, new AttributePool()); //TODO: null constantpool....
    }

    public boolean isVolatile() {
        return is(ACC_VOLATILE);
    }

    public void setVolatile(boolean i) {
        flag = i ? flag | ACC_VOLATILE : flag & ~ACC_VOLATILE;
    }

    public boolean isTransient() {
        return is(ACC_TRANSIENT);
    }

    public void setTransient(boolean i) {
        flag = i ? flag | ACC_TRANSIENT : flag & ~ACC_TRANSIENT;
    }
}
