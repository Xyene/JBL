package com.github.Icyene.BytecodeStudio.Disassembler;

import java.io.*;
import java.util.Arrays;

public class Bytes {

    public static byte[] prepend(byte[] arr, byte... firstElement) {
        return concat(firstElement, arr);
    }

    public static byte[] concat(byte[] A, byte... B) {
        byte[] C = new byte[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    public static short readShort(byte[] bytes, int start) {
        return (short) ((bytes[start] << 8) + bytes[start + 1]);
    }

    public static int readInt(byte[] bytes, int start) {
        int ret = 0;
        for (int i = 0; i != 4; i++) {
            ret <<= 8;
            ret |= (int) bytes[i + start] & 0xFF;
        }
        return ret;
    }

    public static byte[] getShort(short in) {
        return new byte[]{(byte) (in >> 8), (byte) in};
    }

    public static byte[] getInt(int in) {
        return new byte[]{(byte) (in >>> 24), (byte) (in >>> 16), (byte) (in >>> 8), (byte) in};
    }

    public static byte[] slice(byte[] bytes, int start, int end) {
        return Arrays.copyOfRange(bytes, start, end);
    }

    public static byte[] read(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
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

    public static void writeBytesToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
    }
}