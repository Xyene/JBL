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

/**
 * A class to base all attributes on, and in the darkness bind them.
 */
public class Attribute {
    protected String name;
    protected byte[] bytes;

    public Attribute(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public Attribute(String name) {
        this(name, new byte[0]);
    }

    public Attribute() {
    }

    public void dump(ByteStream out, ConstantPool constants) {
        // There is no issue in writing an empty byte array, it saves from having to create an UnknownAttribute
        // class to handle this case.
        out.writeShort(constants.newUTF(name)).writeInt(bytes.length).writeBytes(bytes);
    }

    public final String getName() {
        return name;
    }

    public final void setName(String newName) {
        name = newName;
    }

    @Override
    public String toString() {
        return "{" + name + "}";
    }
}