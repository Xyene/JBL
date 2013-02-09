package tk.jblib.bytecode.introspection;

import tk.jblib.bytecode.introspection.members.Constant;
import tk.jblib.bytecode.introspection.pools.AttributePool;
import tk.jblib.bytecode.introspection.pools.ConstantPool;
import tk.jblib.bytecode.introspection.pools.InterfacePool;
import tk.jblib.bytecode.introspection.pools.MemberPool;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static tk.jblib.bytecode.introspection.metadata.Opcode.TAG_UTF_STRING;

/**
 * A class file structure, used for introspection and modification of classes.
 */
public class ClassFile extends AccessibleMember {

    protected short majorVersion;
    protected short minorVersion;
    protected ConstantPool constantPool;
    protected Constant thisClass;
    protected Constant superClass;
    protected InterfacePool interfacePool;
    protected MemberPool fieldPool;
    protected MemberPool methodPool;
    protected AttributePool attributePool;
    private int loaded = 0;
    private ByteStream classStream;

    /**
     * Constructs a class file object.
     *
     * @param bytes The bytes to construct this object from.
     */
    public ClassFile(byte[] bytes) {
        if ((classStream = new ByteStream(bytes)).readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not start with magic number 0xCAFEBABE");
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
        ensureLoaded(7);
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(0xCAFEBABE));
        out.write(Bytes.toByteArray(minorVersion));
        out.write(Bytes.toByteArray(majorVersion));
        out.write(constantPool.getBytes());
        out.write(Bytes.toByteArray((short) flag));
        out.write(Bytes.toByteArray((short) thisClass.getIndex()));
        out.write(Bytes.toByteArray((short) superClass.getIndex()));
        out.write(interfacePool.getBytes());
        out.write(fieldPool.getBytes());
        out.write(methodPool.getBytes());
        out.write(attributePool.getBytes());
        return out.toByteArray();
    }

    /**
     * Defines a Class from this class file.
     *
     * @return a defined class.
     */
    public Class define() {
        return new ClassLoader() {
            public Class defineClass(byte[] bytes) {
                return super.defineClass(null, bytes, 0, bytes.length);
            }
        }.defineClass(getBytes());
    }

    /**
     * Fetches the major version of this class.
     *
     * @return the major version of this class.
     */
    public short getMajorVersion() {
        ensureLoaded(1);
        return majorVersion;
    }

    /**
     * Sets the major version of this class.
     *
     * @param version the version to set it to.
     */
    public void setMajorVersion(short version) {
        majorVersion = version;
    }

    /**
     * Fetches the minor version of this class.
     *
     * @return the minor version of this class.
     */
    public short getMinorVersion() {
        ensureLoaded(1);
        return minorVersion;
    }

    /**
     * Sets the minor version of this class.
     *
     * @param version the version to set it to.
     */
    public void setMinorVersion(short version) {
        minorVersion = version;
    }

    /**
     * Returns this class' constant pool.
     *
     * @return a constant pool.
     */
    public ConstantPool getConstantPool() {
        ensureLoaded(2);
        return constantPool;
    }

    /**
     * Sets this class' constant pool.
     *
     * @param constantPool the pool to set it to.
     */
    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    /**
     * Returns the fully qualified name of this class.
     *
     * @return the fully qualified name of this class.
     */
    public String getName() {
        ensureLoaded(3);
        return thisClass.getStringValue();
    }

    /**
     * Sets the name of this class.
     *
     * @param clazz The new name for this class.
     */
    public void setThisClass(String clazz) {
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
        return superClass.getStringValue();
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
    public InterfacePool getInterfacePool() {
        ensureLoaded(4);
        return interfacePool;
    }

    /**
     * Sets this class' interface pool.
     *
     * @param interfacePool the pool to set it to.
     */
    public void setInterfacePool(InterfacePool interfacePool) {
        this.interfacePool = interfacePool;
    }

    /**
     * Returns this class' field pool.
     *
     * @return a field pool.
     */
    public MemberPool getFieldPool() {
        ensureLoaded(5);
        return fieldPool;
    }

    /**
     * Sets this class' field pool.
     *
     * @param fieldPool the pool to set it to.
     */
    public void setFieldPool(MemberPool fieldPool) {
        this.fieldPool = fieldPool;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public MemberPool getMethodPool() {
        ensureLoaded(6);
        return methodPool;
    }

    /**
     * Sets this class' method pool.
     *
     * @param methodPool the pool to set it to.
     */
    public void setMethodPool(MemberPool methodPool) {
        this.methodPool = methodPool;
    }

    /**
     * Returns this class' method pool.
     *
     * @return a method pool.
     */
    public AttributePool getAttributePool() {
        ensureLoaded(7);
        return attributePool;
    }

    /**
     * Sets this class' attribute pool.
     *
     * @param attributePool the pool to set it to.
     */
    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }

    protected void ensureLoaded(int pos) {
        if (pos > loaded) {
            switch (pos) {
                case 1:
                    minorVersion = classStream.readShort();
                    majorVersion = classStream.readShort();
                    break;
                case 2:
                    ensureLoaded(1);
                    constantPool = new ConstantPool(classStream);
                    break;
                case 3:
                    ensureLoaded(2);
                    flag = classStream.readShort();
                    thisClass = constantPool.get(classStream.readShort());
                    superClass = constantPool.get(classStream.readShort());
                    break;
                case 4:
                    ensureLoaded(3);
                    interfacePool = new InterfacePool(classStream, constantPool);
                    break;
                case 5:
                    ensureLoaded(4);
                    fieldPool = new MemberPool(classStream, constantPool, this);
                    break;
                case 6:
                    ensureLoaded(5);
                    methodPool = new MemberPool(classStream, constantPool, this);
                    break;
                case 7:
                    ensureLoaded(6);
                    attributePool = new AttributePool(classStream, constantPool);
                    break;
                default:
                    classStream = null; //Clean up, at least partially
                    return;
            }
            loaded++;
        }
    }
}