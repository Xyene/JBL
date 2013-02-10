package tk.jblib.bytecode.introspection;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;
import tk.jblib.bytecode.introspection.metadata.Metadatable;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static tk.jblib.bytecode.introspection.Opcode.TAG_UTF_STRING;

/**
 * A class file structure, used for introspection and modification of classes.
 */
public class ClassFile extends AccessibleMember implements Metadatable<Attribute>{

    protected int majorVersion;
    protected int minorVersion;
    protected Pool<Constant> constantPool;
    protected Constant thisClass;
    protected Constant superClass;
    protected Pool<Interface> interfacePool;
    protected Pool<Member> fieldPool;
    protected Pool<Member> methodPool;
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

        constantPool = new Pool<Constant>(in, Pool.CONSTANT_PARSER);

        flag = in.readShort();
        thisClass = constantPool.get(in.readShort());
        superClass = constantPool.get(in.readShort());

        interfacePool = new Pool<Interface>(in, Pool.INTERFACE_PARSER, constantPool);
        fieldPool = new Pool<Member>(in, Pool.MEMBER_PARSER, constantPool);
        methodPool = new Pool<Member>(in, Pool.MEMBER_PARSER, constantPool);
        metadata = new Metadatable.Container(new Pool<Attribute>(in, Pool.ATTRIBUTE_PARSER, constantPool), constantPool);
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

    /**
     * {@inheritDoc}
     */
    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(0xCAFEBABE));
        out.write(Bytes.toByteArray((short) minorVersion));
        out.write(Bytes.toByteArray((short) majorVersion));
        out.write(constantPool.getBytes());
        out.write(Bytes.toByteArray((short) flag));
        out.write(Bytes.toByteArray((short) thisClass.getIndex()));
        out.write(Bytes.toByteArray((short) superClass.getIndex()));
        out.write(interfacePool.getBytes());
        out.write(fieldPool.getBytes());
        out.write(methodPool.getBytes());
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
    public Pool<Constant> getConstantPool() {
        return constantPool;
    }

    /**
     * Sets this class' constant pool.
     *
     * @param constantPool the pool to set it to.
     */
    public void setConstantPool(Pool<Constant> constantPool) {
        this.constantPool = constantPool;
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
    public Pool<Interface> getInterfacePool() {
        return interfacePool;
    }

    /**
     * Sets this class' interface pool.
     *
     * @param interfacePool the pool to set it to.
     */
    public void setInterfacePool(Pool<Interface> interfacePool) {
        this.interfacePool = interfacePool;
    }

    /**
     * Returns this class' field pool.
     *
     * @return a field pool.
     */
    public Pool<Member> getFieldPool() {
        return fieldPool;
    }

    /**
     * Sets this class' field pool.
     *
     * @param fieldPool the pool to set it to.
     */
    public void setFieldPool(Pool<Member> fieldPool) {
        this.fieldPool = fieldPool;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public Pool<Member> getMethodPool() {
        return methodPool;
    }

    /**
     * Sets this class' method pool.
     *
     * @param methodPool the pool to set it to.
     */
    public void setMethodPool(Pool<Member> methodPool) {
        this.methodPool = methodPool;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public Pool<Attribute> getAttributePool() {
        return metadata.getAttributes();
    }

    /**
     * Sets this class' attribute pool.
     *
     * @param attributePool the pool to set it to.
     */
    public void setAttributePool(Pool<Attribute> attributePool) {
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
