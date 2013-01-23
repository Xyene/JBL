package com.github.Icyene.bytecode.introspection.util;

import java.io.*;
import java.util.Arrays;

/**
 * Utility class for operations on byte( array)s.
 */
public class Bytes {

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
        return (short) (((bytes[start] & 0xFF) << 8) + (bytes[(start + 1)] & 0xFF));
    }

    public static int toInteger(byte[] bytes, int start) {
        int ret = 0;
        for (int i = 0; i != 4; i++)
            ret = ret << 8 | bytes[(i + start)] & 0xFF;
        return ret;
    }

    public static long toLong(byte[] bytes, int start) {
        return ((bytes[start] & 0xFFL) << 56) |
                ((bytes[start + 1] & 0xFFL) << 48) |
                ((bytes[start + 2] & 0xFFL) << 40) |
                ((bytes[start + 3] & 0xFFL) << 32) |
                ((bytes[start + 4] & 0xFFL) << 24) |
                ((bytes[start + 5] & 0xFFL) << 16) |
                ((bytes[start + 6] & 0xFFL) << 8) |
                ((bytes[start + 7] & 0xFFL) << 0);
    }

    public static double toDouble(byte[] bytes, int start) {
        return Double.longBitsToDouble(toLong(bytes, start));
    }

    public static byte[] toByteArray(double d) {
        long l = Double.doubleToRawLongBits(d);
        return new byte[]{
                (byte) ((l >> 56) & 0xFF),
                (byte) ((l >> 48) & 0xFF),
                (byte) ((l >> 40) & 0xFF),
                (byte) ((l >> 32) & 0xFF),
                (byte) ((l >> 24) & 0xFF),
                (byte) ((l >> 16) & 0xFF),
                (byte) ((l >> 8) & 0xFF),
                (byte) ((l >> 0) & 0xFF),
        };
    }

    public static byte[] toByteArray(short in) {
        return new byte[]{(byte) (in >> 8), (byte) in};
    }

    public static byte[] toByteArray(int in) {
        return new byte[]{(byte) (in >> 24), (byte) (in >>> 16), (byte) (in >>> 8), (byte) in};
    }

    public static byte[] slice(byte[] bytes, int start, int end) {
        return Arrays.copyOfRange(bytes, start, end);
    }

    public static byte[] read(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[655653];
        int nRead;
        while ((nRead = stream.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, nRead);
        return buffer.toByteArray();
    }

    public static byte[] read(File file) throws IOException {
        return read(new FileInputStream(file));
    }

    public static String bytesToString(byte... bytes) {
        StringBuilder build = new StringBuilder();
       for(int i = 0; i != bytes.length; i++) {
            build.append(bytes[i]).append(i < bytes.length-1 ? ", " : "");
       }
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