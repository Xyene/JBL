package com.github.Icyene.bytecode.introspection.internal;

import com.github.Icyene.bytecode.introspection.internal.members.Constant;
import com.github.Icyene.bytecode.introspection.internal.pools.*;
import com.github.Icyene.bytecode.introspection.util.ByteStream;
import com.github.Icyene.bytecode.introspection.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClassFile {

    protected short majorVersion;
    protected short minorVersion;
    protected AccessFlag accessFlags;
    protected ConstantPool constantPool;
    protected Constant thisClass;
    protected Constant superClass;
    protected InterfacePool interfacePool;
    protected PropertyPool fieldPool;
    protected PropertyPool methodPool;
    protected AttributePool attributePool;

    public ClassFile(byte[] bytes) {
        ByteStream stream = new ByteStream(bytes);
        if (stream.readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");

        minorVersion = stream.readShort();
        majorVersion = stream.readShort();
        constantPool = new ConstantPool(stream);
        accessFlags = new AccessFlag(stream.readShort());
        System.out.println("Access flags = " + accessFlags.getMask());
        thisClass = constantPool.get(stream.readShort() );
        superClass = constantPool.get(stream.readShort() );
        interfacePool = new InterfacePool(stream, constantPool);
        fieldPool = new PropertyPool(stream, constantPool);
        methodPool = new PropertyPool(stream, constantPool);
        attributePool = new AttributePool(stream, constantPool);
    }

    public ClassFile(InputStream stream) throws IOException {
        this(Bytes.read(stream));
    }

    public ClassFile(File f) throws IOException {
        this(Bytes.read(f));
    }

    public byte[] getBytes() {
        ByteStream out = new ByteStream();
        out.write(Bytes.toByteArray(0xCAFEBABE));
        out.write(Bytes.toByteArray(minorVersion));
        out.write(Bytes.toByteArray(majorVersion));
        out.write(constantPool.getBytes());
        out.write(accessFlags.getBytes());
        out.write(Bytes.toByteArray((short) thisClass.getIndex()));
        out.write(Bytes.toByteArray((short) superClass.getIndex()));
        out.write(interfacePool.getBytes());
        out.write(fieldPool.getBytes());
        out.write(methodPool.getBytes());
        out.write(attributePool.getBytes());
        return out.toByteArray();
    }

    public short getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(short version) {
        majorVersion = version;
    }

    public short getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(short version) {
        minorVersion = version;
    }

    public AccessFlag getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(AccessFlag accessFlag) {
        accessFlags = accessFlag;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = new AccessFlag(accessFlags);
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    public Constant getThisClass() {
        return thisClass;
    }

    public void setThisClass(Constant thisClass) {
        this.thisClass = thisClass;
    }

    public Constant getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Constant superClass) {
        this.superClass = superClass;
    }

    public InterfacePool getInterfacePool() {
        return interfacePool;
    }

    public void setInterfacePool(InterfacePool interfacePool) {
        this.interfacePool = interfacePool;
    }

    public PropertyPool getFieldPool() {
        return fieldPool;
    }

    public void setFieldPool(PropertyPool fieldPool) {
        this.fieldPool = fieldPool;
    }

    public PropertyPool getMethodPool() {
        return methodPool;
    }

    public void setMethodPool(PropertyPool methodPool) {
        this.methodPool = methodPool;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}