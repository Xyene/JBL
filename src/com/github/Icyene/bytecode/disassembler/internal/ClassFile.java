package com.github.Icyene.bytecode.disassembler.internal;

import com.github.Icyene.bytecode.disassembler.internal.attributes.SourceFileAttribute;
import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.pools.*;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClassFile {

    private short majorVersion;
    private short minorVersion;
    private AccessFlag accessFlags;
    private ConstantPool constantPool;
    private Constant thisClass;
    private Constant superClass;
    private InterfacePool interfacePool;
    private PropertyPool fieldPool;
    private PropertyPool methodPool;
    private AttributePool attributePool;
    private SourceFileAttribute sourceFile;

    public ClassFile(byte[] bytes) {
        ByteStream stream = new ByteStream(bytes);
        if (stream.readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");

        minorVersion = stream.readShort();
        majorVersion = stream.readShort();
        constantPool = new ConstantPool(stream);
        accessFlags = new AccessFlag(stream.readShort());
        thisClass = constantPool.get(stream.readShort() );
        superClass = constantPool.get(stream.readShort() );
        interfacePool = new InterfacePool(stream, constantPool);
        fieldPool = new PropertyPool(stream, constantPool);
        methodPool = new PropertyPool(stream, constantPool);
        attributePool = new AttributePool(stream, constantPool);
        sourceFile = (SourceFileAttribute) attributePool.getInstancesOf(SourceFileAttribute.class).get(0);

       // Pool<Constant> cpool = new Pool<Constant>(stream, null);
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

    public String getSourceFile() {
        return sourceFile.getSource();
    }

    public void setSourceFile(String source) {
        sourceFile.setSource(source);
    }
}