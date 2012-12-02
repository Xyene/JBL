package com.github.Icyene.BytecodeStudio.Disassembler;

import com.github.Icyene.BytecodeStudio.Disassembler.Pools.ConstantPool;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Flag;
import com.github.Icyene.BytecodeStudio.Disassembler.Pools.InterfacePool;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Constant;

import java.io.File;
import java.io.IOException;

public class ClassFile {

    private byte[] bytes;

    private short majorVersion;
    private short minorVersion;
    private Flag accessFlags;
    private ConstantPool constantPool;
    private Constant thisClass;
    private Constant superClass;
    private InterfacePool interfacePool;

    public ClassFile(File f) throws IOException {
        bytes = Bytes.read(f);
        if (Bytes.readInt(bytes, 0) != 0xCAFEBABE)
            throw new IllegalStateException("File does not contain magic number 0xCAFEBABE");
        setMinorVersion(Bytes.readShort(bytes, 4));
        setMajorVersion(Bytes.readShort(bytes, 6));
        setConstantPool(new ConstantPool(bytes));

        int offset = getConstantPool().getOffset();

        setAccessFlags(new Flag(Bytes.readShort(bytes, offset)));
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
        raw = Bytes.append(raw, getAccessFlags().assemble());
        raw = Bytes.append(raw, Bytes.getShort((short) getThisClass().getIndex()));
        raw = Bytes.append(raw, Bytes.getShort((short) getSuperClass().getIndex()));
        raw = Bytes.append(raw, getInterfacePool().assemble());
        return raw;
    }

    short getMajorVersion() {
        return majorVersion;
    }

    void setMajorVersion(short majorVersion) {
        this.majorVersion = majorVersion;
    }

    short getMinorVersion() {
        return minorVersion;
    }

    void setMinorVersion(short minorVersion) {
        this.minorVersion = minorVersion;
    }

    Flag getAccessFlags() {
        return accessFlags;
    }

    void setAccessFlags(Flag accessFlags) {
        this.accessFlags = accessFlags;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    void setConstantPool(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    Constant getThisClass() {
        return thisClass;
    }

    void setThisClass(Constant thisClass) {
        this.thisClass = thisClass;
    }

    Constant getSuperClass() {
        return superClass;
    }

    void setSuperClass(Constant superClass) {
        this.superClass = superClass;
    }

    InterfacePool getInterfacePool() {
        return interfacePool;
    }

    void setInterfacePool(InterfacePool interfacePool) {
        this.interfacePool = interfacePool;
    }
}