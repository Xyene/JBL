package com.github.Icyene.bytecode.introspection.util;

import java.util.Arrays;

/**
 * A container designed around the Bytes utility class.
 */
public class ByteStream {
    private transient byte[] bytes;
    private transient int pos = 0;

    public ByteStream(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteStream() {
        this.bytes = new byte[]{};
    }

    public ByteStream(byte[] bytes, int index) {
        this.bytes = bytes;
        this.pos = index;
    }

    public ByteStream write(byte... byt) {
        bytes = Bytes.concat(bytes, byt);
        return this;
    }

    public ByteStream write(String s) {
        for (char c : s.toCharArray()) {
            write(c);
        }
        return this;
    }

    public ByteStream write(int i) {
        write(Bytes.toByteArray(i));
        return this;
    }

    public ByteStream write(short s) {
        write((char) s);
        return this;
    }

    public ByteStream write(char c) {
        write(Bytes.toByteArray((short) c));
        return this;
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
        return (readInt() << 32) + (readInt());
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

    public ByteStream seek(int pos) {
        this.pos = pos;
        return this;
    }

    public int position() {
        return pos;
    }

    public void backtrack(int n) {
        pos = pos - n;
    }

    public int remaining() {
        return bytes.length - pos;
    }
}

