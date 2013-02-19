package net.sf.jbl.introspection;

import net.sf.jbl.util.ByteStream;

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
}