package net.sf.jbl.util;

import java.io.*;
import java.util.Arrays;

/**
 * A one-size-fit-all IO stream
 */
public class ByteStream {
    protected byte[] buffer;
    protected volatile int _in = 0;
    protected volatile int _out = 0;

    protected int flag;

    protected final static String err = "stream ended prematurely";

    ByteStream(int flag) {
        this.flag = flag;
    }

    public static ByteStream writeStream(int buffer) {
        ByteStream stream = new ByteStream(0);
        stream.buffer = new byte[buffer];//new DataOutputStream(stream.array = new ByteArrayOutputStream(buffer));
        return stream;
    }

    public static ByteStream writeStream() {
        return writeStream(0);
    }

    public static ByteStream readStream(byte[] bytes, int pos) {
        ByteStream stream = new ByteStream(1);
        stream._in = pos;
        stream.buffer = bytes;
        return stream;
    }

    public static ByteStream readStream(byte[] bytes) {
        return readStream(bytes, 0);
    }

    public static ByteStream readStream(File file) {
        try {
            return readStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("stream ended prematurely", e);
        }
    }

    public static ByteStream readStream(InputStream io) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[655653];
        int nRead;
        try {
            while ((nRead = io.read(data, 0, data.length)) != -1)
                buffer.write(data, 0, nRead);
        } catch (IOException e) {
            throw new IllegalStateException("stream ended prematurely", e);
        }
        return readStream(buffer.toByteArray());
    }

    public boolean isRead() {
        return flag == 1;
    }

    public boolean isWrite() {
        return flag == 0;
    }

    public void enlarge(int size) {
        int length1 = buffer.length << 1;
        int length2 = _out + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        System.arraycopy(buffer, 0, newData, 0, _out);
        buffer = newData;
    }

    public ByteStream writeInt(final int i) {
        int length = _out;
        if (length + 4 > buffer.length) {
            enlarge(4);
        }
        byte[] data = buffer;
        data[length++] = (byte) (i >>> 24);
        data[length++] = (byte) (i >>> 16);
        data[length++] = (byte) (i >>> 8);
        data[length++] = (byte) i;
        return this;
    }

    public ByteStream writeLong(final long l) {
        writeInt((int) (l >>> 32));
        writeInt((int) l);
        return this;
    }

    public ByteStream writeDouble(double d) {
        return writeLong(Double.doubleToLongBits(d));
    }

    public final ByteStream writeUTF(String s) {
        writeShort(s.length());
        int charLength = s.length();
        int len = _out;
        if (len + 2 + charLength > buffer.length) {
            enlarge(charLength + 2);
        }

        byte[] data = buffer;
        buffer[len++] = (byte) (charLength >>> 8);
        data[len++] = (byte) charLength;

        _top:
        for (int i = 0; i < charLength; ++i) {
            char c;
            if ((c = s.charAt(i)) <= 0 || c > 127) {
                int byteLength = i;
                int j;
                for (j = i; j < charLength; ++j) {
                    if ((c = s.charAt(j)) > 0 && c <= 127) {
                        ++byteLength;
                    } else if (c > 2047) {
                        byteLength += 3;
                    } else {
                        byteLength += 2;
                    }
                }
                data[len] = (byte) (byteLength >>> 8);
                data[len++] = (byte) byteLength;
                if (len + 2 + byteLength > data.length) {
                    enlarge(byteLength + 2);
                    data = buffer;
                }
                j = i;
                while (true) {
                    j++;
                    if (j >= charLength) {
                        break _top;
                    }

                    if ((c = s.charAt(j)) > 0 && c <= 127) {
                        data[len++] = (byte) c;
                        continue;
                    } else if (c > 2047) {
                        data[len++] = (byte) (224 | c >> 12 & 15);
                        data[len++] = (byte) (128 | c >> 6 & 63);
                    } else {
                        data[len++] = (byte) (192 | c >> 6 & 31);
                    }
                    data[len++] = (byte) (128 | c & 63);
                }
            }
            data[len++] = (byte) c;
        }
        _out = len;
        return this;
    }


    public ByteStream writeShort(int s) {
        int length = _out;
        if (length + 2 > buffer.length) {
            enlarge(2);
        }
        byte[] data = buffer;
        data[length++] = (byte) (s >>> 8);
        data[length++] = (byte) s;
        _out = length;
        return this;
    }

    public ByteStream writeByte(int b) {
        int length = _out;
        if (length + 2 > buffer.length) {
            enlarge(2);
        }
        buffer[length++] = (byte) b;
        this._out = length;
        return this;
    }

    public ByteStream writeBytes(byte[] b) {
        if (b.length > 0) {
            if (_out + b.length > buffer.length) {
                enlarge(b.length);
            }
            System.arraycopy(b, 0, buffer, _out, b.length);
            _out += b.length;
        }
        return this;
    }

    public boolean readBoolean() {
        return readByte() == 1 ? true : false;
    }

    public int readUnsignedByte() {
        return buffer[_in++];
    }

    public short readShort() {
        byte[] b = buffer;
        return (short) (((b[_in++] & 0xFF) << 8) | (b[_in++] & 0xFF));
    }

    public int readUnsignedShort() {
        byte[] b = buffer;
        return ((b[_in++] & 0xFF) << 8) | (b[_in++] & 0xFF);
    }

    public int readInt() {
        byte[] b = buffer;
        return ((b[_in++] & 0xFF) << 24) | ((b[_in++] & 0xFF) << 16) | ((b[_in++] & 0xFF) << 8) | (b[_in++] & 0xFF);
    }

    public long readLong() {
        return (readInt() << 32) | readInt() & 0xFFFFFFFFL;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public byte readByte() {
        return (byte) (buffer[_in++] & 0xFF);
    }

    public byte[] read(int n) {
        byte[] arr = new byte[n];
        System.arraycopy(buffer, _in, arr, 0, n);
        _in += n;
        return arr;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int position() {
        return isWrite() ? _out : _in;
    }

    public String toString() {
        return "{ByteStream" + Arrays.toString(buffer) + "}";
    }
}

