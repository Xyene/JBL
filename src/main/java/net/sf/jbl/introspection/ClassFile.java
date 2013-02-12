package net.sf.jbl.introspection;

import net.sf.jbl.introspection.members.Attribute;
import net.sf.jbl.introspection.members.Constant;
import net.sf.jbl.introspection.members.Interface;
import net.sf.jbl.introspection.metadata.Metadatable;
import net.sf.jbl.util.ByteStream;
import net.sf.jbl.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static net.sf.jbl.introspection.Opcode.TAG_UTF_STRING;

/**
 * A class file structure, used for introspection and modification of classes.
 */
public class ClassFile extends AccessibleMember implements Metadatable<Attribute> {

    protected int majorVersion;
    protected int minorVersion;
    protected Pool<Constant> constants;
    protected Constant thisClass;
    protected Constant superClass;
    protected Pool<Interface> interfaces;
    protected Pool<Member> fields;
    protected Pool<Member> methods;
    protected Metadatable.Container metadata;

    /**
     * Constructs a class file object.
     *
     * @param bytes The bytes to construct this object from.
     */
    public ClassFile(byte[] bytes) {
        ByteStream in = new ByteStream(bytes);
        if (in.readInt() != 0xCAFEBABE)
            throw new ClassFormatError("File does not contain magic number 0xCAFEBABE");

        minorVersion = in.readShort();
        majorVersion = in.readShort();
        if(minorVersion > Opcode.JDK_8)
            throw new UnsupportedOperationException("unknown major version: " + majorVersion);

        constants = new Pool<Constant>(in, Pool.CONSTANT_PARSER);

        flag = in.readShort();
        thisClass = constants.get(in.readShort());
        superClass = constants.get(in.readShort());

        interfaces = new Pool<Interface>(in, Pool.INTERFACE_PARSER, constants);
        fields = new Pool<Member>(in, Pool.MEMBER_PARSER, constants);
        methods = new Pool<Member>(in, Pool.MEMBER_PARSER, constants);
        metadata = new Metadatable.Container(new Pool<Attribute>(in, Pool.ATTRIBUTE_PARSER, constants), constants);
    }

    /**
     * Constructs a class file object.
     *
     * @param stream The stream to construct this object from.
     */
    public ClassFile(InputStream stream) throws IOException {
        this(Bytes.read(stream));
    }

    /**
     * Constructs a class file object.
     *
     * @param file The file to construct this object from.
     */
    public ClassFile(File file) throws IOException {
        this(Bytes.read(file));
    }

    public ClassFile() {
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(0xCAFEBABE);
        out.write((short) minorVersion);
        out.write((short) majorVersion);
        out.write(constants.getBytes());
        out.write((short) flag);
        out.write((short) thisClass.getIndex());
        out.write((short) superClass.getIndex());
        out.write(interfaces.getBytes());
        out.write(fields.getBytes());
        out.write(methods.getBytes());
        out.write(metadata.getAttributes().getBytes());
        return out.toByteArray();
    }

    /**
     * Defines a Class from this class file.
     *
     * @return a defined class.
     */
    public Class define(final String name) {
        return new ClassLoader() {
            public Class defineClass(byte[] bytes) {
                return super.defineClass(name, bytes, 0, bytes.length);
            }
        }.defineClass(getBytes());
    }

    /**
     * Fetches the major version of this class.
     *
     * @return the major version of this class.
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Sets the major version of this class.
     *
     * @param version the version to set it to.
     */
    public void setMajorVersion(int version) {
        majorVersion = version;
    }

    /**
     * Fetches the minor version of this class.
     *
     * @return the minor version of this class.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the minor version of this class.
     *
     * @param version the version to set it to.
     */
    public void setMinorVersion(int version) {
        minorVersion = version;
    }

    /**
     * Returns this class' constant pool.
     *
     * @return a constant pool.
     */
    public Pool<Constant> getConstants() {
        return constants;
    }

    /**
     * Sets this class' constant pool.
     *
     * @param constants the pool to set it to.
     */
    public void setConstants(Pool<Constant> constants) {
        this.constants = constants;
    }

    /**
     * Returns the fully qualified name of this class.
     *
     * @return the fully qualified name of this class.
     */
    public String getName() {
        return thisClass.stringValue();
    }

    /**
     * Sets the name of this class.
     *
     * @param clazz The new name for this class.
     */
    public void setName(String clazz) {
        thisClass.getOwner().set(thisClass.getIndex(), (thisClass = new Constant(thisClass.getIndex(), TAG_UTF_STRING, clazz.getBytes())));
    }

    /**
     * Returns the fully qualified name of this class' superclass.
     *
     * @return the fully qualified name of this class' superclass.
     */
    public String getSuperClass() {
        return superClass.stringValue();
    }

    /**
     * Sets the name of this class' superclass.
     *
     * @param superclass The new name for this class' superclass.
     */
    public void setSuperClass(String superclass) {
        superClass.getOwner().set(superClass.getIndex(), (superClass = new Constant(superClass.getIndex(), TAG_UTF_STRING, superclass.getBytes())));
    }

    /**
     * Returns this class' interface pool.
     *
     * @return an interface pool.
     */
    public Pool<Interface> getInterfaces() {
        return interfaces;
    }

    /**
     * Sets this class' interface pool.
     *
     * @param interfaces the pool to set it to.
     */
    public void setInterfaces(Pool<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * Returns this class' field pool.
     *
     * @return a field pool.
     */
    public Pool<Member> getFields() {
        return fields;
    }

    /**
     * Sets this class' field pool.
     *
     * @param fields the pool to set it to.
     */
    public void setFields(Pool<Member> fields) {
        this.fields = fields;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public Pool<Member> getMethods() {
        return methods;
    }

    /**
     * Sets this class' method pool.
     *
     * @param methods the pool to set it to.
     */
    public void setMethods(Pool<Member> methods) {
        this.methods = methods;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public Pool<Attribute> getAttributes() {
        return metadata.getAttributes();
    }

    /**
     * Sets this class' attribute pool.
     *
     * @param attributePool the pool to set it to.
     */
    public void setAttributes(Pool<Attribute> attributePool) {
        metadata.setAttributes(attributePool);
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
