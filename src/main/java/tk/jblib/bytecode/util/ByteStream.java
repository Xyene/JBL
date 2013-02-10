package tk.jblib.bytecode.util;

import java.io.*;
import java.util.Arrays;

/**
 * A container designed around the Bytes utility class.
 */
public class ByteStream implements Flushable {
    private int pos = 0;
    private DataInputStream in;
    private DataOutputStream out;
    private ByteArrayOutputStream array;

    public ByteStream(byte[] bytes) {
        this(bytes, 0);
    }

    public ByteStream() {
        this(new byte[0]);
    }

    public ByteStream(byte[] bytes, int index) {
        pos = index;
        in = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(bytes), 8192));
        out = new DataOutputStream(new BufferedOutputStream(array = new ByteArrayOutputStream(8192), 8192));
        try {
            in.skipBytes(index);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public ByteStream write(byte... byt) {
        try {
            out.write(byt);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return this;
    }

    public ByteStream write(byte byt) {
        try {
            out.write(byt);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return this;
    }

    public ByteStream write(String utf) {
        try {
            out.writeUTF(utf);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return this;
    }

    public ByteStream write(int i) {
        try {
            out.writeInt(i);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return this;
    }

    public ByteStream write(short s) {
        try {
            out.writeShort(s);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return this;
    }

    public ByteStream write(char c) {
        try {
            out.writeChar(c);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return this;
    }

    public boolean readBoolean() {
        try {
            pos += 1;
            return in.readBoolean();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public int readUnsignedByte() {
        try {
            pos += 1;
            return in.readUnsignedByte();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public short readShort() {
        try {
            pos += 2;
            return in.readShort();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public int readUnsignedShort() {
        try {
            pos += 2;
            return in.readUnsignedShort();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public char readChar() {
        try {
            pos += 2;
            return in.readChar();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public int readInt() {
        try {
            pos += 4;
            return in.readInt();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public long readLong() {
        try {
            pos += 8;
            return in.readLong();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public float readFloat() {
        try {
            pos += 4;
            return in.readFloat();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public double readDouble() {
        try {
            pos += 8;
            return in.readDouble();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public byte readByte() {
        try {
            pos += 1;
            return in.readByte();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public byte[] read(int n) {
        byte[] arr = new byte[n];
        for(int i = 0; i != n; i++)
            arr[i] = readByte();
        pos += n;
        return arr;
    }

    public byte[] toByteArray() {
        try {
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return array.toByteArray();
    }

    public int position() {
        return pos;
    }

    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
    }

    public byte[] readFully() {
        byte[] out = new byte[0];
        try {
            in.readFully(out);
        } catch (IOException e) {
            throw new IllegalStateException("data stream ended prematurely", e);
        }
        return out;
    }

    public String toString() {
        return "{ByteStream" + Bytes.bytesToString(toByteArray()) + "}";
    }
}

