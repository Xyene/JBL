package tk.jblib.bytecode.introspection;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.metadata.Metadatable;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.Collection;

import static tk.jblib.bytecode.introspection.Opcode.*;

/**
 * A generic class member. Can refer to either a field or a member, depending on the pool it is generated from.
 */
public class Member extends AccessibleMember implements Metadatable<Attribute>{
    protected Constant name;
    protected Constant descriptor;
    protected Metadatable.Container metadata;

    /**
     * Constructs a member.
     *
     * @param stream The stream of bytes containing member data.
     * @param pool   The associated constant pool.
     */
    public Member(ByteStream stream, Pool<Constant> pool) {
        flag = stream.readShort();
        name = pool.get(stream.readShort());
        descriptor = pool.get(stream.readShort());
        metadata = new Metadatable.Container(new Pool<Attribute>(stream, Pool.ATTRIBUTE_PARSER, pool), pool);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray((short) flag));
        out.write(Bytes.toByteArray((short) name.getIndex()));
        out.write(Bytes.toByteArray((short) descriptor.getIndex()));
        out.write(metadata.getAttributes().getBytes());
        return out.toByteArray();
    }

    /**
     * Gets the name of this member.
     *
     * @return the name of this member.
     */
    public String getName() {
        return name.stringValue();
    }

    /**
     * Sets this member's name.
     *
     * @param newName the new name of the member.
     */
    public void setName(String newName) {
        name.getOwner().set(name.getIndex(), (name = new Constant(TAG_UTF_STRING, newName.getBytes())));
    }

    /**
     * Gets the descriptor of this member.
     *
     * @return the descriptor of this member.
     */
    public String getDescriptor() {
        return descriptor.stringValue();
    }

    /**
     * Sets this member's type descriptor.
     *
     * @param newDescriptor the new descriptor of the member.
     */
    public void setDescriptor(String newDescriptor) {
        descriptor.getOwner().set(descriptor.getIndex(), (descriptor = new Constant(TAG_UTF_STRING, newDescriptor.getBytes())));
    }

    /**
     * Returns the attribute pool of this member.
     *
     * @return the attribute pool of this member.
     */
    public Pool<Attribute> getAttributePool() {
        return metadata.getAttributes();
    }

    /**
     * Sets the attribute pool of this member.
     *
     * @param attributePool the new attribute pool.
     */
    public void setAttributePool(Pool<Attribute> attributePool) {
        metadata.setAttributes(attributePool);
    }

    /**
     * Checks if this member is marked deprecated (@Deprecated annotation or @deprecated JavaDoc tag).
     *
     * @return True if this member is deprecated, false otherwise.
     */
    public boolean isDeprecated() {
        return metadata.getAttributes().contains("Deprecated");
    }

    /**
     * Toggles this member's deprecated status.
     *
     * @param flag True if intent is to make this member deprecated, false if it is to make it not deprecated.
     */
    public void setDeprecated(boolean flag) {
        if (flag && !metadata.getAttributes().contains("Deprecated")) {
            metadata.addMetadata("Deprecated", null);
        } else {
            metadata.removeMetadata("Deprecated");
        }
    }

    /**
     * Is this member synchronized?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isSynchronized() {
        return is(ACC_SYNCHRONIZED);
    }

    /**
     * Toggles synchronization of this member.
     *
     * @param i True if intent is to mark member synchronized, false otherwise.
     */
    public void setSynchronized(boolean i) {
        flag = i ? flag | ACC_SYNCHRONIZED : flag & ~ACC_SYNCHRONIZED;
    }

    /**
     * Is this member native?
     *
     * @return True if it is, false otherwise.
     */
    public boolean isNative() {
        return is(ACC_NATIVE);
    }

    /**
     * Toggles native flag of this member.
     *
     * @param i True if intent is to mark member native, false otherwise.
     */
    public void setNative(boolean i) {
        flag = i ? flag | ACC_NATIVE : flag & ~ACC_NATIVE;
    }

    @Override
    public Collection<Attribute> getMetadataInstances(String meta) {
        return metadata.getAttributes();
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
}
