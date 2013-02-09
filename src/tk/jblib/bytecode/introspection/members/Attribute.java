package tk.jblib.bytecode.introspection.members;

import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import static tk.jblib.bytecode.introspection.metadata.Opcode.TAG_UTF_STRING;

/**
 * A class to base all attributes off of, and in the darkness bind them.
 */
public class Attribute {

    protected int length;
    protected Constant name;

    /**
     * Creates an attribute with the given parameters.
     *
     * @param name   the name of the attribute.
     * @param length the length, in bytes, of the attribute.
     */
    public Attribute(Constant name, int length) {
        this.name = name;
        this.length = length;
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public Attribute() {
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        return Bytes.concat(Bytes.toByteArray((short) name.getIndex()), Bytes.toByteArray(length));
    }

    /**
     * Fetches the name of this attribute.
     *
     * @return the name of this attribute.
     */
    public final String getName() {
        return name.getStringValue();
    }

    /**
     * Sets the name of this attribute.
     *
     * @param newName the new name.
     */
    public final void setName(String newName) {
        name.getOwner().set(name.getIndex(), (name = new Constant(TAG_UTF_STRING, newName.getBytes())));
    }

    /**
     * Fetches the length, in bytes, of this attribute.
     *
     * @return the length.
     */
    public final int getLength() {
        return length;
    }
}