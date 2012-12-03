package com.github.Icyene.BytecodeStudio.Disassembler;

import com.github.Icyene.BytecodeStudio.Disassembler.Pools.ConstantPool;
import com.github.Icyene.BytecodeStudio.Disassembler.Pools.InterfacePool;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Constant;
import com.github.Icyene.BytecodeStudio.Disassembler.Types.Flag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClassFile {

    private byte[] bytes;

    private short majorVersion;
    private short minorVersion;
    private Flag accessFlags;
    private ConstantPool constantPool;
    private Constant thisClass;
    private Constant superClass;
    private InterfacePool interfacePool;

    public ClassFile(byte[] bytes) {
        this.bytes = bytes;
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

    public ClassFile(InputStream stream) throws IOException {
        this(Bytes.read(stream));
    }

    public ClassFile(File f) throws IOException {
        this(Bytes.read(f));
    }

    public byte[] assemble() {
        byte[] raw = Bytes.getInt(0xCAFEBABE);
        raw = Bytes.concat(raw, Bytes.getShort(getMinorVersion()));
        raw = Bytes.concat(raw, Bytes.getShort(getMajorVersion()));
        raw = Bytes.concat(raw, getConstantPool().assemble());
        raw = Bytes.concat(raw, getAccessFlags().assemble());
        raw = Bytes.concat(raw, Bytes.getShort((short) getThisClass().getIndex()));
        raw = Bytes.concat(raw, Bytes.getShort((short) getSuperClass().getIndex()));
        raw = Bytes.concat(raw, getInterfacePool().assemble());
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