package tk.jblib.bytecode.introspection.members.attributes;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

/**
 * An unknown attribute, defaulted to if attribute cannot be recognized.
 */
public class UnknownAttribute extends Attribute {

    private byte[] value;

    /**
     * Constructs an UnknownAttribute attribute, default.
     * @param stream stream containing encoded data.
     * @param name the name.
     */
    public UnknownAttribute(ByteStream stream, Constant name) {
        super(name, stream.readInt());
        value = stream.read(length);
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public UnknownAttribute() {}

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        length = value.length;
        return Bytes.concat(super.getBytes(), value);
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
