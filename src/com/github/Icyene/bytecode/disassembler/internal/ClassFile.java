package com.github.Icyene.bytecode.disassembler.internal;

import com.github.Icyene.bytecode.disassembler.internal.objects.Constant;
import com.github.Icyene.bytecode.disassembler.internal.objects.Flag;
import com.github.Icyene.bytecode.disassembler.internal.pools.ConstantPool;
import com.github.Icyene.bytecode.disassembler.internal.pools.InterfacePool;
import com.github.Icyene.bytecode.disassembler.util.ByteStream;
import com.github.Icyene.bytecode.disassembler.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClassFile {

    private short majorVersion;
    private short minorVersion;
    private Flag accessFlags;
    private ConstantPool constantPool;
    private Constant thisClass;
    private Constant superClass;
    private InterfacePool interfacePool;

    public ClassFile(byte[] bytes) {
        ByteStream stream = new ByteStream(bytes);
        if (stream.readInt() != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");

        minorVersion = stream.readShort();
        majorVersion = stream.readShort();
        constantPool = new ConstantPool(stream);
        accessFlags = new Flag(stream.readShort());
        thisClass = constantPool.get(stream.readShort());
        superClass = constantPool.get(stream.readShort());
        interfacePool = new InterfacePool(stream, constantPool);
    }

    public ClassFile(InputStream stream) throws IOException {
        this(Bytes.read(stream));
    }

    public ClassFile(File f) throws IOException {
        this(Bytes.read(f));
    }

    public byte[] assemble() {
        ByteStream out = new ByteStream(Bytes.toByteArray(0xCAFEBABE));
        out.write(Bytes.toByteArray(minorVersion));
        out.write(Bytes.toByteArray(majorVersion));
        out.write(constantPool.assemble());
        out.write(accessFlags.assemble());
        out.write(Bytes.toByteArray((short) thisClass.getIndex()));
        out.write(Bytes.toByteArray((short) superClass.getIndex()));
        out.write(interfacePool.assemble());
        return out.toByteArray();
    }

    public short getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(short majorVersion) {
        this.majorVersion = majorVersion;
    }

    public short getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(short minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Flag getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(Flag accessFlags) {
        this.accessFlags = accessFlags;
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
}