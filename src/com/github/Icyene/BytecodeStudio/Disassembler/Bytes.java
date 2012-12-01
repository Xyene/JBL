package com.github.Icyene.BytecodeStudio.Disassembler;

import java.io.*;
import java.util.Arrays;

public class Bytes {

    public static byte[] prepend(byte[] arr, byte firstElement) {
        byte[] ret = new byte[arr.length + 1];
        ret[0] = firstElement;
        for (int i = 0; i != arr.length; i++)
            ret[i + 1] = arr[i];
        return ret;
    }

    public static byte[] append(byte[] one, byte... two) {
        byte[] ret = new byte[one.length + two.length];
        for (int i = 0; i != one.length; i++)
            ret[i] = one[i];
        for (int i = 0; i != two.length; i++)
            ret[i + one.length] = two[i];
        return ret;
    }

    public static short readShort(byte[] bytes, int start) {
        return (short) ((bytes[start] << 8) + bytes[start + 1]);
    }

    public static int readInt(byte[] bytes, int start) {
            int ret = 0;
            for (int i=0; i<4 && i+start<bytes.length; i++) {
                ret <<= 8;
                ret |= (int)bytes[i] & 0xFF;
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

    public static byte[] read(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
        bin.read(buffer);
        bin.close();
        return buffer;
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