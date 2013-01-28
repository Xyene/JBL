package tk.jblib.bytecode.introspection.code;

import tk.jblib.bytecode.introspection.members.TryCatch;
import tk.jblib.bytecode.util.ByteStream;
import tk.jblib.bytecode.util.Bytes;

import java.util.ArrayList;

/**
 * A pool to hold all Try/Catch structures.
 */
public class ExceptionPool extends ArrayList<TryCatch> {

    /**
     * Constructs an exception pool.
     *
     * @param stream The stream of bytes containing the pool data.
     */
    public ExceptionPool(ByteStream stream) {
        short size = stream.readShort();
        for (int i = 0; i != size; i++)
            add(new TryCatch(stream));
    }

    /**
     * Public no-args constructor for extending classes. Should not be used directly.
     */
    public ExceptionPool() {
    }

    /**
     * Gets a byte[] representation of this object.
     *
     * @return a byte[] representation of this object.
     */
    public byte[] getBytes() {
        byte[] raw = Bytes.toByteArray((short) size());
        for (TryCatch e : this)
            raw = Bytes.concat(raw, e.getBytes());
        return raw;
    }

    public void getBytes(ByteStream stream) {
        stream.write(getBytes());
    }
}
