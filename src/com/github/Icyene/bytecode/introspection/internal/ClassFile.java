package com.github.Icyene.bytecode.introspection.internal;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.AttributePool;
import com.github.Icyene.bytecode.introspection.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.introspection.internal.pools.InterfacePool;
import com.github.Icyene.bytecode.introspection.internal.pools.MemberPool;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.github.Icyene.bytecode.introspection.internal.metadata.Opcode.TAG_UTF_STRING;

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

    /**
     * Constructs a class file object.
     *
     * @param bytes The bytes to construct this object from.
     */
    public ClassFile(byte[] bytes) {
        ByteStream stream = new ByteStream(bytes);
        if (stream.readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");

        minorVersion = stream.readShort();
        majorVersion = stream.readShort();
        constantPool = new ConstantPool(stream);
        flag = stream.readShort();
        thisClass = constantPool.get(stream.readShort());
        superClass = constantPool.get(stream.readShort());
        interfacePool = new InterfacePool(stream, constantPool);
        fieldPool = new MemberPool(stream, constantPool, this);
        methodPool = new MemberPool(stream, constantPool, this);
        attributePool = new AttributePool(stream, constantPool);
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
        return thisClass.getStringValue();
    }

    /**
     * Sets the name of this class.
     *
     * @param clazz The new name for this class.
     */
    public void setThisClass(String clazz) {
        thisClass.getOwner().set(thisClass.getIndex(), (thisClass = new Constant(thisClass.getIndex(), TAG_UTF_STRING, clazz.getBytes())));
    }

    /**
     * Returns the fully qualified name of this class' superclass.
     *
     * @return the fully qualified name of this class' superclass.
     */
    public String getSuperClass() {
        return superClass.getStringValue();
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
    public InterfacePool getInterfacePool() {
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
}