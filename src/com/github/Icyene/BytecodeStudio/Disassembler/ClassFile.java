package com.github.Icyene.BytecodeStudio.Disassembler;

import com.github.Icyene.BytecodeStudio.Disassembler.Indices.ConstantPoolIndex;
import com.github.Icyene.BytecodeStudio.Disassembler.Pools.ConstantPool;
import com.github.Icyene.BytecodeStudio.Disassembler.Pools.InterfacePool;

import java.io.File;
import java.io.IOException;

public class ClassFile {

    private byte[] bytes;

    private short majorVersion;
    private short minorVersion;
    private short accessFlags;
    private ConstantPool constantPool;
    private ConstantPoolIndex thisClass;
    private ConstantPoolIndex superClass;
    private InterfacePool interfacePool;

    public ClassFile(File f) throws IOException {
        bytes = Bytes.read(f);
        if (Bytes.readInt(bytes, 0) != 0xCAFEBABE)  //3405691582
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");
        setMinorVersion(Bytes.readShort(bytes, 4));
        setMajorVersion(Bytes.readShort(bytes, 6));
        setConstantPool(new ConstantPool(bytes));

        int offset = getConstantPool().getOffset();

        setAccessFlags(Bytes.readShort(bytes, offset));
        setThisClass(getConstantPool().get(Bytes.readShort(bytes, 2 + offset)));  //12, 13
        setSuperClass(getConstantPool().get(Bytes.readShort(bytes, 4 + offset))); //13, 14

        setInterfacePool(new InterfacePool(bytes, getConstantPool()));
    }

    public static ClassFile get(File f) throws IOException {
        return new ClassFile(f);
    }

    public byte[] assemble() {
        byte[] raw = Bytes.getInt(0xCAFEBABE);
        raw = Bytes.append(raw, Bytes.getShort(getMinorVersion()));
        raw = Bytes.append(raw, Bytes.getShort(getMajorVersion()));
        raw = Bytes.append(raw, getConstantPool().assemble());
        raw = Bytes.append(raw, Bytes.getShort(getAccessFlags()));
        raw = Bytes.append(raw, Bytes.getShort((short) getThisClass().getIndex()));
        raw = Bytes.append(raw, Bytes.getShort((short) getSuperClass().getIndex()));
        raw = Bytes.append(raw, getInterfacePool().assemble());
        return raw;
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

    public short getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(short accessFlags) {
        this.accessFlags = accessFlags;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    public ConstantPoolIndex getThisClass() {
        return thisClass;
    }

    public void setThisClass(ConstantPoolIndex thisClass) {
        this.thisClass = thisClass;
    }

    public ConstantPoolIndex getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ConstantPoolIndex superClass) {
        this.superClass = superClass;
    }

    public InterfacePool getInterfacePool() {
        return interfacePool;
    }

    public void setInterfacePool(InterfacePool interfacePool) {
        this.interfacePool = interfacePool;
    }
}