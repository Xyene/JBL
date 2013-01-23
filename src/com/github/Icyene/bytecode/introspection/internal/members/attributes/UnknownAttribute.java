package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

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
