package com.github.Icyene.bytecode.disassembler.util;

import java.util.Arrays;

public class ByteStream {
    private transient byte[] bytes;
    private transient int pos = 0;

    public ByteStream(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteStream() {
        this.bytes = new byte[]{};
    }

    public void write(byte... byt) {
        bytes = Bytes.concat(bytes, byt);
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public int readUnsignedByte() {
        return readByte();
    }

    public short readShort() {
        return (short) readUnsignedShort();
    }

    public int readUnsignedShort() {
        return ((readByte() & 0xFF) << 8) + (readByte() & 0xFF);
    }

    public char readChar() {
        return (char) readUnsignedShort();
    }

    public int readInt() {
        return ((readByte() & 0xFF) << 24) + ((readByte() & 0xFF) << 16) + ((readByte() & 0xFF) << 8) + (readByte() & 0xFF);
    }

    public long readLong() {
        return (readInt() << 32) + (readInt() & 0xFFFFFFFF);
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public byte readByte() {
        return bytes[pos++];
    }

    public byte[] read(int n) {
        return Arrays.copyOfRange(bytes, pos, pos += n);
    }

    public byte[] toByteArray() {
        return bytes;
    }

    public void seek(int pos) {
        this.pos = pos;
    }

    public int position() {
        return pos;
    }

    public void backtrack(int n) {
        pos = pos-n;
    }
}

