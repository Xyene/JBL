package tk.jblib.bytecode.introspection;

import tk.jblib.bytecode.introspection.members.Attribute;
import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.members.Interface;
import tk.jblib.bytecode.introspection.metadata.Metadatable;
import tk.jblib.bytecode.introspection.metadata.XMLAttribute;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static tk.jblib.bytecode.introspection.Opcode.TAG_UTF_STRING;

/**
 * A class file structure, used for introspection and modification of classes.
 */
public class ClassFile extends AccessibleMember implements Metadatable<Attribute> {

    protected int majorVersion;
    protected int minorVersion;
    protected Pool<Constant> constantPool;
    protected Constant thisClass;
    protected Constant superClass;
    protected Pool<Interface> interfacePool;
    protected Pool<Member> fieldPool;
    protected Pool<Member> methodPool;
    protected Pool<Attribute> attributePool;
    protected ByteStream classStream;
    private int loaded;

    /**
     * Constructs a class file object.
     *
     * @param bytes The bytes to construct this object from.
     */
    public ClassFile(byte[] bytes) {
        classStream = new ByteStream(bytes);
        if (classStream.readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");
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
        if (loaded >= 1) {
            out.write(Bytes.toByteArray((short)minorVersion));
            out.write(Bytes.toByteArray((short)majorVersion));
        }
        if (loaded >= 2)
            out.write(constantPool.getBytes());
        if (loaded >= 3) {
            out.write(Bytes.toByteArray((short) flag));
            out.write(Bytes.toByteArray((short) thisClass.getIndex()));
            out.write(Bytes.toByteArray((short) superClass.getIndex()));
        }
        if (loaded >= 4)
            out.write(interfacePool.getBytes());
        if (loaded >= 5)
            out.write(fieldPool.getBytes());
        if (loaded >= 6)
            out.write(methodPool.getBytes());
        if (loaded >= 7)
            out.write(attributePool.getBytes());
        if (loaded < 7)
            out.write(classStream.readFully()); //Read the rest of the class
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
        ensureLoaded(1);
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
        ensureLoaded(1);
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
        ensureLoaded(2);
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
        ensureLoaded(3);
        return thisClass.stringValue();
    }

    /**
     * Sets the name of this class.
     *
     * @param clazz The new name for this class.
     */
    public void setName(String clazz) {
        ensureLoaded(3);
        thisClass.getOwner().set(thisClass.getIndex(), (thisClass = new Constant(thisClass.getIndex(), TAG_UTF_STRING, clazz.getBytes())));
    }

    /**
     * Returns the fully qualified name of this class' superclass.
     *
     * @return the fully qualified name of this class' superclass.
     */
    public String getSuperClass() {
        ensureLoaded(3);
        return superClass.stringValue();
    }

    /**
     * Sets the name of this class' superclass.
     *
     * @param superclass The new name for this class' superclass.
     */
    public void setSuperClass(String superclass) {
        ensureLoaded(3);
        superClass.getOwner().set(superClass.getIndex(), (superClass = new Constant(superClass.getIndex(), TAG_UTF_STRING, superclass.getBytes())));
    }

    /**
     * Returns this class' interface pool.
     *
     * @return an interface pool.
     */
    public Pool<Interface> getInterfacePool() {
        ensureLoaded(4);
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
        ensureLoaded(5);
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
        ensureLoaded(6);
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
        ensureLoaded(7);
        return attributePool;
    }

    /**
     * Sets this class' attribute pool.
     *
     * @param attributePool the pool to set it to.
     */
    public void setAttributePool(Pool<Attribute> attributePool) {
        this.attributePool = attributePool;
    }

    public void ensureLoaded(int pos) {
        if (pos > loaded) {
            switch (pos) {
                case 1:
                    minorVersion = classStream.readShort();
                    majorVersion = classStream.readShort();
                    break;
                case 2:
                    ensureLoaded(1);
                    constantPool = new Pool<Constant>(classStream, Pool.CONSTANT_PARSER);
                    break;
                case 3:
                    ensureLoaded(2);
                    flag = classStream.readShort();
                    thisClass = constantPool.get(classStream.readShort());
                    superClass = constantPool.get(classStream.readShort());
                    break;
                case 4:
                    ensureLoaded(3);
                    interfacePool = new Pool<Interface>(classStream, Pool.INTERFACE_PARSER, constantPool);
                    break;
                case 5:
                    ensureLoaded(4);
                    fieldPool = new Pool<Member>(classStream, Pool.MEMBER_PARSER, constantPool);
                    break;
                case 6:
                    ensureLoaded(5);
                    methodPool = new Pool<Member>(classStream, Pool.MEMBER_PARSER, constantPool);
                    break;
                case 7:
                    ensureLoaded(6);
                    attributePool = new Pool<Attribute>(classStream, Pool.ATTRIBUTE_PARSER, constantPool);
                    break;
                default:
                    classStream = null; //Clean up, at least partially
                    return;
            }
            loaded++;
        }
    }

    public Set<Attribute> getMetadataInstances(String meta) {
        Set<Attribute> out = new HashSet<Attribute>();
        for (Attribute a : attributePool)
            if (a.getName().equals(meta))
                out.add(a);
        return out;
    }

    public void removeMetadata(String meta) {
        attributePool.removeAll(getMetadataInstances(meta));
    }

    @Override
    public boolean hasMetadata(String meta) {
        return getMetadataInstances(meta).size() > 0;
    }

    public Object getMetadata(String meta) {
        Attribute ab = getMetadataInstances(meta).iterator().next();
        return ab instanceof XMLAttribute ? ((XMLAttribute) ab).readObject() : ab;
    }

    @Override
    public <V> void addMetadata(String meta, V value) {
        if (value instanceof Attribute)
            attributePool.add((Attribute) value);
        else {
            try {
                attributePool.add(new XMLAttribute<V>(meta, value));
            } catch (IOException e) {
                throw new RuntimeException("could not encode attribute '" + meta + "' with value '" + value + "'", e);
            }
        }
    }
}