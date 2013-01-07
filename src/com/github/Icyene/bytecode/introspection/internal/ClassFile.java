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
import java.util.HashMap;

public class ClassFile extends AccessibleMember {

    protected short majorVersion;
    protected short minorVersion;
    protected ConstantPool constantPool;
    protected Constant thisClass;
    protected Constant superClass;
    protected InterfacePool interfacePool;
    protected MemberPool<Member> fieldPool;
    protected MemberPool<Member> methodPool;
    protected AttributePool attributePool;
    protected final HashMap<String, Object> meta = new HashMap<String, Object>();

    public ClassFile(byte[] bytes) {
        long start;
        start = System.currentTimeMillis();
        ByteStream stream = new ByteStream(bytes);
        System.out.println("Opened stream: " + (System.currentTimeMillis() - start) + "ms");
        if (stream.readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");

        minorVersion = stream.readShort();
        majorVersion = stream.readShort();
        handleConstantPool(stream);
        meta.put("accessFlags", flag = stream.readShort());
        meta.put("thisClass", thisClass = constantPool.get(stream.readShort()));
        meta.put("superClass", superClass = constantPool.get(stream.readShort()));

        handleInterfacePool(stream);
        handleFieldPool(stream);
        handleMethodPool(stream);
        handleAttributePool(stream);
    }

    public ClassFile(InputStream stream) throws IOException {
        this(Bytes.read(stream));
    }

    protected void handleConstantPool(ByteStream stream) {
        long start;
        start = System.currentTimeMillis();
        constantPool = new ConstantPool(stream);
        System.out.println("Constant pool: " + (System.currentTimeMillis() - start) + "ms");
    }

    protected void handleInterfacePool(ByteStream stream) {
        long start;
        start = System.currentTimeMillis();
        interfacePool = new InterfacePool(stream, constantPool);
        System.out.println("Interface pool: " + (System.currentTimeMillis() - start) + "ms");
    }

    protected void handleFieldPool(ByteStream stream) {
        long start;
        start = System.currentTimeMillis();
        fieldPool = new MemberPool<Member>(stream, constantPool, this);
        System.out.println("Field pool: " + (System.currentTimeMillis() - start) + "ms");
    }

    protected void handleMethodPool(ByteStream stream) {
        long start;
        start = System.currentTimeMillis();
        methodPool = new MemberPool<Member>(stream, constantPool, this);
        System.out.println("Method pool: " + (System.currentTimeMillis() - start) + "ms");
    }

    protected void handleAttributePool(ByteStream stream) {
        long start;
        start = System.currentTimeMillis();
        attributePool = new AttributePool(stream, constantPool);
        System.out.println("Attribute pool: " + (System.currentTimeMillis() - start) + "ms");
    }

    public HashMap<String, Object> getMetadata() {
        return meta;
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
        out.write(Bytes.toByteArray((short)flag));
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

    public MemberPool<Member> getFieldPool() {
        return fieldPool;
    }

    public void setFieldPool(MemberPool fieldPool) {
        this.fieldPool = fieldPool;
    }

    public MemberPool<Member> getMethodPool() {
        return methodPool;
    }

    public void setMethodPool(MemberPool methodPool) {
        this.methodPool = methodPool;
    }

    public AttributePool getAttributePool() {
        return attributePool;
    }

    public void setAttributePool(AttributePool attributePool) {
        this.attributePool = attributePool;
    }
}