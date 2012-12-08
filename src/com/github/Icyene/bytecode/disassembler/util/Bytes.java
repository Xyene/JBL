package com.github.Icyene.bytecode.disassembler.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Bytes {  //TODO MAKE NOT USE NIO!!!!

    public static byte[] prepend(byte[] A, byte... B) {
        return concat(B, A);
    }

    public static byte[] concat(byte[] A, byte... B) {
        byte[] C = new byte[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    public static short toShort(byte[] bytes, int start) {
        return (short) ((bytes[start] << 8) + bytes[(start + 1)]);
    }

    public static int toInteger(byte[] bytes, int start) {
        int ret = 0;
        for (int i = 0; i != 4; i++)
            ret = ret << 8 | bytes[(i + start)] & 0xFF;
        return ret;
    }

    public static double toDouble(byte[] bytes, int start) {
        return ByteBuffer.wrap(Arrays.copyOfRange(bytes, start, start + 8)).getDouble();
    }

    public static byte[] toByteArray(short in) {
        return new byte[]{(byte) (in >> 8), (byte) in};
    }

    public static byte[] toByteArray(int in) {
        return new byte[]{(byte) (in >> 24), (byte) (in >>> 16), (byte) (in >>> 8), (byte) in};
    }

    private static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static byte[] slice(byte[] bytes, int start, int end) {
        return Arrays.copyOfRange(bytes, start, end);
    }

    public static byte[] read(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = stream.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, nRead);
        return buffer.toByteArray();
    }

    public static byte[] read(File file) throws IOException {
        return read(new FileInputStream(file));
    }

    public static String bytesToString(byte[] bytes) {
        StringBuilder build = new StringBuilder();
        for (byte bi : bytes)
            build.append(bi).append(", ");
        return "[" + build.toString() + "]";
    }

    public static void writeBytesToFile(byte[] bytes, String file) throws IOException {
        writeBytesToFile(bytes, new File(file));
    }

    private static void writeBytesToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
    }
}