package com.github.Icyene.bytecode.introspection.internal.members.attributes;

import com.github.Icyene.bytecode.introspection.internal.members.Attribute;
import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_INTEGER;

/**
 * A constant value attribute, used to designate a field which has its static or final flag set.
 */
public class ConstantValue extends Attribute {
    private Constant constantIndex;


    /**
     * Constructs a ConstantValue attribute.
     *
     * @param stream the stream where value data is encoded.
     * @param name   the name of the attribute, "ConstantValue".
     * @param pool   the associated constant pool.
     */
    public ConstantValue(ByteStream stream, Constant name, ConstantPool pool) {
        super(name, stream.readInt());
        constantIndex = pool.get(stream.readShort());
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public ConstantValue() {
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) constantIndex.getIndex()));
        byte[] bytes = out.toByteArray();
        length = bytes.length;
        return Bytes.prepend(bytes, super.getBytes());
    }

    /**
     * Returns the constant pool index referenced by this attribute.
     *
     * @return a constant.
     */
    public int getConstantIndex() {
        return constantIndex.getIndex();
    }

    /**
     * Sets the index referenced by this attribute.
     *
     * @param index the new index.
     */
    public void setConstantIndex(int index) {
        constantIndex.getOwner().set(constantIndex.getIndex(), (constantIndex = new Constant(constantIndex.getIndex(), TAG_INTEGER, Bytes.toByteArray((short) index))));
    }
}
